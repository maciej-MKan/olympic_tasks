package pl.mkan.battle_on_chessboard.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mkan.battle_on_chessboard.controller.dto.Color;
import pl.mkan.battle_on_chessboard.controller.dto.GameDTO;
import pl.mkan.battle_on_chessboard.controller.dto.UnitDTO;
import pl.mkan.battle_on_chessboard.service.GameService;

import java.util.List;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/new")
    public ResponseEntity<GameDTO> startNewGame() {
        GameDTO game = gameService.startNewGame();
        return ResponseEntity.ok(game);
    }

    @GetMapping("/{gameId}/units")
    public ResponseEntity<List<UnitDTO>> getUnits(@PathVariable Long gameId, @RequestParam Color color) {
        return ResponseEntity.ok(gameService.getUnits(gameId, color));
    }

    @PostMapping("/{gameId}/command")
    public ResponseEntity<Void> executeCommand(@PathVariable Long gameId, @RequestParam Long unitId, @RequestParam Color color, @RequestParam String command, @RequestParam String direction, @RequestParam int distance) {
        gameService.executeCommand(gameId, unitId, color, command, direction, distance);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{gameId}/unit/{unitId}/random-command")
    public ResponseEntity<Void> executeRandomCommand(
            @PathVariable Long gameId,
            @PathVariable Long unitId,
            @RequestParam Color color) {
        gameService.executeRandomCommand(gameId, unitId, color);
        return ResponseEntity.ok().build();
    }
}
