package pl.mkan.battle_on_chessboard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.mkan.battle_on_chessboard.controller.dto.Color;
import pl.mkan.battle_on_chessboard.controller.dto.GameDTO;
import pl.mkan.battle_on_chessboard.controller.dto.UnitDTO;
import pl.mkan.battle_on_chessboard.persistence.model.Unit;
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

        int originalX = archer.x();
        int expectedX = originalX + 1 < 10 ? originalX + 1 : 9;

        gameService.executeCommand(game.id(), archer.id(), Color.WHITE, "move", "right", 1);

        Unit updatedArcher = unitRepository.findById(archer.id()).orElse(null);

        assertNotNull(updatedArcher);
        assertEquals(expectedX, updatedArcher.getX());
    }

    @Test
    public void testExecuteRandomCommand() {
        GameDTO game = gameService.startNewGame();

        List<UnitDTO> whiteUnits = gameService.getUnits(game.id(), Color.WHITE);
        UnitDTO archer = whiteUnits.stream().filter(unit -> unit.type().equals("ARCHER")).findFirst().orElse(null);

        assertNotNull(archer);

        gameService.executeRandomCommand(game.id(), archer.id(), Color.WHITE);

        Unit updatedArcher = unitRepository.findById(archer.id()).orElse(null);

        assertNotNull(updatedArcher);
    }

    @Test
    public void testMoveUnitOutOfBounds() {
        GameDTO game = gameService.startNewGame();

        List<UnitDTO> whiteUnits = gameService.getUnits(game.id(), Color.WHITE);
        UnitDTO archer = whiteUnits.stream().filter(unit -> unit.type().equals("ARCHER")).findFirst().orElse(null);

        assertNotNull(archer);

        // Move archer to the edge of the board
        gameService.executeCommand(game.id(), archer.id(), Color.WHITE, "move", "right", 9);

        Unit updatedArcher = unitRepository.findById(archer.id()).orElse(null);

        assertNotNull(updatedArcher);
        assertEquals(9, updatedArcher.getX()); // Archer should be at the right edge

        // Wait for the cooldown time
        waitUntilNextCommand();

        // Try to move archer out of bounds
        gameService.executeCommand(game.id(), updatedArcher.getId(), Color.WHITE, "move", "right", 1);
        updatedArcher = unitRepository.findById(archer.id()).orElse(null);

        assert updatedArcher != null;
        assertEquals(9, updatedArcher.getX()); // Archer should still be at the right edge
    }

    @Test
    public void testUnitCollision() {
        GameDTO game = gameService.startNewGame();

        List<UnitDTO> whiteUnits = gameService.getUnits(game.id(), Color.WHITE);
        UnitDTO transport = whiteUnits.stream().filter(unit -> unit.type().equals("TRANSPORT")).findFirst().orElse(null);

        assertNotNull(transport);

        // Move transport to a position
        gameService.executeCommand(game.id(), transport.id(), Color.WHITE, "move", "right", 9);
        waitUntilNextCommand();
        gameService.executeCommand(game.id(), transport.id(), Color.WHITE, "move", "down", 9);
        Unit updatedTransport = unitRepository.findById(transport.id()).orElse(null);

        assertNotNull(updatedTransport);
        int newX = updatedTransport.getX();
        int newY = updatedTransport.getY();

        // Wait for the cooldown time
        waitUntilNextCommand();

        // Place another white unit at the same position
        UnitDTO anotherTransport = whiteUnits.stream().filter(
                unit -> unit.type().equals("TRANSPORT") && !unit.id().equals(transport.id()) && unit.color().equals(Color.WHITE)
        ).findFirst().orElse(null);
        assertNotNull(anotherTransport);

        gameService.executeCommand(game.id(), anotherTransport.id(), Color.WHITE, "move", "right", 9);
        waitUntilNextCommand();
        gameService.executeCommand(game.id(), anotherTransport.id(), Color.WHITE, "move", "down", 9);
        Unit updatedAnotherTransport = unitRepository.findById(anotherTransport.id()).orElse(null);

        assert updatedAnotherTransport != null;
        assertEquals(newX, updatedAnotherTransport.getX());
        assertEquals(newY, updatedAnotherTransport.getY());

        // Ensure the first transport did not move
        updatedTransport = unitRepository.findById(transport.id()).orElse(null);
        assert updatedTransport != null;
        assertEquals(newX, updatedTransport.getX());
        assertEquals(newY, updatedTransport.getY());
    }

    private void waitUntilNextCommand() {
        try {
            Thread.sleep(17100); // Assuming the cooldown time is 1 second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}