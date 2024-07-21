package pl.mkan.battle_on_chessboard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.mkan.battle_on_chessboard.controller.dto.Color;
import pl.mkan.battle_on_chessboard.controller.dto.GameDTO;
import pl.mkan.battle_on_chessboard.controller.dto.UnitDTO;
import pl.mkan.battle_on_chessboard.persistence.entity.Unit;
import pl.mkan.battle_on_chessboard.persistence.repository.UnitRepository;
import pl.mkan.battle_on_chessboard.service.GameService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GameServiceTests {

    @Autowired
    private GameService gameService;

    @Autowired
    private UnitRepository unitRepository;

    @Test
    public void testStartNewGame() {
        GameDTO game = gameService.startNewGame();

        assertNotNull(game.id());
        assertEquals("active", game.status());

        List<Unit> unitsWhite = unitRepository.findByGameIdAndColor(game.id(), Color.WHITE);
        List<Unit> unitsBlack = unitRepository.findByGameIdAndColor(game.id(), Color.BLACK);

        long whiteUnits = unitsWhite.size();
        long blackUnits = unitsBlack.size();

        assertEquals(11, whiteUnits);
        assertEquals(11, blackUnits);
    }

    @Test
    public void testGetUnits() {
        GameDTO game = gameService.startNewGame();

        List<UnitDTO> whiteUnits = gameService.getUnits(game.id(), Color.WHITE);
        List<UnitDTO> blackUnits = gameService.getUnits(game.id(), Color.BLACK);

        assertEquals(11, whiteUnits.size());
        assertEquals(11, blackUnits.size());
    }

    @Test
    public void testExecuteCommand() {
        GameDTO game = gameService.startNewGame();

        List<UnitDTO> whiteUnits = gameService.getUnits(game.id(), Color.WHITE);
        UnitDTO archer = whiteUnits.stream().filter(unit -> unit.type().equals("ARCHER")).findFirst().orElse(null);

        assertNotNull(archer);

        gameService.executeCommand(game.id(), archer.id(), Color.WHITE, "move", "right", 1);

        Unit updatedArcher = unitRepository.findById(archer.id()).orElse(null);

        assertNotNull(updatedArcher);
        assertEquals(archer.x() + 1, updatedArcher.getX());
    }
}
