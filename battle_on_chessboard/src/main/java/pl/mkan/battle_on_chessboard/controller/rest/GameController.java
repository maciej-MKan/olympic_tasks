package pl.mkan.battle_on_chessboard.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mkan.battle_on_chessboard.persistence.entity.Unit;
import pl.mkan.battle_on_chessboard.service.GameService;

import java.util.List;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/new")
    public ResponseEntity<Void> startNewGame() {
        gameService.startNewGame();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/units")
    public ResponseEntity<List<Unit>> getUnits(@RequestParam String color) {
        return ResponseEntity.ok(gameService.getUnits(color));
    }

    @PostMapping("/command")
    public ResponseEntity<Void> executeCommand(@RequestParam Long unitId, @RequestParam String command, @RequestParam String direction, @RequestParam int distance) {
        gameService.executeCommand(unitId, command, direction, distance);
        return ResponseEntity.ok().build();
    }
}
