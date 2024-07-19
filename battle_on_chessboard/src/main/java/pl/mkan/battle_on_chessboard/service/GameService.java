package pl.mkan.battle_on_chessboard.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.mkan.battle_on_chessboard.persistence.entity.Unit;
import pl.mkan.battle_on_chessboard.persistence.entity.UnitStatus;
import pl.mkan.battle_on_chessboard.persistence.entity.UnitType;
import pl.mkan.battle_on_chessboard.persistence.repository.UnitRepository;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GameService {

    private final UnitRepository unitRepository;

    @Value("${settings.board.width}")
    private int boardWidth;
    @Value("${settings.board.height}")
    private int boardHeight;
    @Value("${settings.unit.archer.count}")
    private int ArcherCount;
    @Value("${settings.unit.cannon.count}")
    private int CannonCount;
    @Value("${settings.unit.transport.count}")
    private int TransportCount;

    @Transactional
    public void startNewGame() {
        // Remove old game data
        unitRepository.deleteAll();

        // Randomly place units on the board
        // Example for placing 4 archers, 2 cannons, 5 transports
        placeUnitsRandomly(UnitType.ARCHER, ArcherCount);
        placeUnitsRandomly(UnitType.CANNON, CannonCount);
        placeUnitsRandomly(UnitType.TRANSPORT, TransportCount);
    }

    private void placeUnitsRandomly(UnitType type, int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            Unit unit = new Unit();
            unit.setType(type.name());
            unit.setX(random.nextInt(boardWidth));
            unit.setY(random.nextInt(boardHeight));
            unit.setStatus(UnitStatus.ACTIVE.name());
            unit.setMoves(0);
            unitRepository.save(unit);
        }
    }

    public List<Unit> getUnits(String color) {
        // Fetch units based on color (white/black)
        return unitRepository.findAll();
    }

    @Transactional
    public void executeCommand(Long unitId, String command, String direction, int distance) {
        Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new RuntimeException("Unit not found"));
        // Process the command
        // Example for archer movement
        if (command.equals("move") && unit.getType().equals(UnitType.ARCHER.name())) {
            // Move logic
        }
        // Save the updated unit
        unitRepository.save(unit);
    }
}
