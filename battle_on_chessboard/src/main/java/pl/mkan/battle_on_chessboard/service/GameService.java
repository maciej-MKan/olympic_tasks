package pl.mkan.battle_on_chessboard.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.mkan.battle_on_chessboard.controller.dto.Color;
import pl.mkan.battle_on_chessboard.controller.dto.GameDTO;
import pl.mkan.battle_on_chessboard.controller.dto.UnitDTO;
import pl.mkan.battle_on_chessboard.persistence.entity.Game;
import pl.mkan.battle_on_chessboard.persistence.entity.Unit;
import pl.mkan.battle_on_chessboard.persistence.entity.UnitStatus;
import pl.mkan.battle_on_chessboard.persistence.entity.UnitType;
import pl.mkan.battle_on_chessboard.persistence.repository.GameRepository;
import pl.mkan.battle_on_chessboard.persistence.repository.UnitRepository;
import pl.mkan.battle_on_chessboard.service.mapper.GameMapper;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final UnitRepository unitRepository;
    private final GameMapper gameMapper;

    @Value("${settings.board.width}")
    private int boardWidth;

    @Value("${settings.board.height}")
    private int boardHeight;

    @Value("${settings.unit.archer.count}")
    private int archerCount;

    @Value("${settings.unit.cannon.count}")
    private int cannonCount;

    @Value("${settings.unit.transport.count}")
    private int transportCount;

    private final Object lock = new Object();

    @Transactional
    public GameDTO startNewGame() {
        synchronized (lock) {
            Game game = new Game();
            game.setStatus("active");
            game = gameRepository.save(game);

            placeUnitsRandomly(game, UnitType.ARCHER, archerCount, Color.WHITE);
            placeUnitsRandomly(game, UnitType.CANNON, cannonCount, Color.WHITE);
            placeUnitsRandomly(game, UnitType.TRANSPORT, transportCount, Color.WHITE);

            placeUnitsRandomly(game, UnitType.ARCHER, archerCount, Color.BLACK);
            placeUnitsRandomly(game, UnitType.CANNON, cannonCount, Color.BLACK);
            placeUnitsRandomly(game, UnitType.TRANSPORT, transportCount, Color.BLACK);

            return gameMapper.toDto(game);
        }
    }

    private void placeUnitsRandomly(Game game, UnitType type, int count, Color color) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            Unit unit = new Unit();
            unit.setType(UnitType.valueOf(type.name()));
            unit.setColor(color);
            unit.setX(random.nextInt(boardWidth));
            unit.setY(random.nextInt(boardHeight));
            unit.setStatus(UnitStatus.ACTIVE.name());
            unit.setMoves(0);
            unit.setGame(game);
            unitRepository.save(unit);
        }
    }

    @Transactional
    public List<UnitDTO> getUnits(Long gameId, Color color) {
        synchronized (lock) {
            List<Unit> units = unitRepository.findByGameIdAndColor(gameId, color);
            return units.stream().map(gameMapper::toDto).collect(Collectors.toList());
        }
    }

    @Transactional
    public void executeCommand(Long gameId, Long unitId, Color color, String command, String direction, int distance) {
        synchronized (lock) {
            Unit unit = unitRepository.findByIdAndGameIdAndColor(unitId, gameId, color)
                    .orElseThrow(() -> new EntityNotFoundException("Unit not found"));

            // Process the command
            processCommand(unit, command, direction, distance);

            // Save the updated unit
            unitRepository.save(unit);
        }
    }

    private void processCommand(Unit unit, String command, String direction, int distance) {
        switch (unit.getType()) {
            case ARCHER -> processArcherCommand(unit, command, direction, distance);
            case TRANSPORT -> processTransportCommand(unit, command, direction, distance);
            case CANNON -> processCannonCommand(unit, command, direction, distance);
        }
    }

    private void processArcherCommand(Unit unit, String command, String direction, int distance) {
        switch (command.toLowerCase()) {
            case "move" -> {
                moveUnit(unit, direction, 1);
                unit.setMoves(unit.getMoves() + 1);
            }
            case "shoot" -> {
                int targetX = unit.getX();
                int targetY = unit.getY();

                switch (direction.toLowerCase()) {
                    case "up" -> targetY -= distance;
                    case "down" -> targetY += distance;
                    case "left" -> targetX -= distance;
                    case "right" -> targetX += distance;
                }

                if (isEnemyUnitAtPosition(unit, targetX, targetY)) {
                    Unit targetUnit = getEnemyUnitAtPosition(unit, targetX, targetY);
                    targetUnit.setStatus(UnitStatus.DESTROYED.name());
                }
            }
        }
    }

    private void processTransportCommand(Unit unit, String command, String direction, int distance) {
        if ("move".equalsIgnoreCase(command)) {
            moveUnit(unit, direction, distance);
            unit.setMoves(unit.getMoves() + 1);

            if (isEnemyUnitAtPosition(unit, unit.getX(), unit.getY())) {
                Unit targetUnit = getEnemyUnitAtPosition(unit, unit.getX(), unit.getY());
                targetUnit.setStatus(UnitStatus.DESTROYED.name());
            }
        }
    }

    private void processCannonCommand(Unit unit, String command, String direction, int distance) {
        if ("shoot".equalsIgnoreCase(command)) {
            int[] targetCoordinates = calculateDiagonalShot(unit.getX(), unit.getY(), direction, distance);
            int targetX = targetCoordinates[0];
            int targetY = targetCoordinates[1];

            if (isEnemyUnitAtPosition(unit, targetX, targetY)) {
                Unit targetUnit = getEnemyUnitAtPosition(unit, targetX, targetY);
                targetUnit.setStatus(UnitStatus.DESTROYED.name());
            }
        }
    }

    private void moveUnit(Unit unit, String direction, int distance) {
        switch (direction.toLowerCase()) {
            case "up" -> unit.setY(Math.max(0, unit.getY() - distance));
            case "down" -> unit.setY(Math.min(boardHeight - 1, unit.getY() + distance));
            case "left" -> unit.setX(Math.max(0, unit.getX() - distance));
            case "right" -> unit.setX(Math.min(boardWidth - 1, unit.getX() + distance));
        }
    }

    private boolean isEnemyUnitAtPosition(Unit unit, int x, int y) {
        return unit.getGame().getUnits().stream()
                .anyMatch(u -> u.getX() == x && u.getY() == y && u.getColor() != unit.getColor());
    }

    private Unit getEnemyUnitAtPosition(Unit unit, int x, int y) {
        return unit.getGame().getUnits().stream()
                .filter(u -> u.getX() == x && u.getY() == y && u.getColor() != unit.getColor())
                .findFirst()
                .orElse(null);
    }

    private int[] calculateDiagonalShot(int startX, int startY, String direction, int distance) {
        int targetX = startX;
        int targetY = startY;

        switch (direction.toLowerCase()) {
            case "up-left" -> {
                targetX -= distance;
                targetY -= distance;
            }
            case "up-right" -> {
                targetX += distance;
                targetY -= distance;
            }
            case "down-left" -> {
                targetX -= distance;
                targetY += distance;
            }
            case "down-right" -> {
                targetX += distance;
                targetY += distance;
            }
        }

        targetX = Math.max(0, Math.min(targetX, boardWidth - 1));
        targetY = Math.max(0, Math.min(targetY, boardHeight - 1));

        return new int[]{targetX, targetY};
    }
}
