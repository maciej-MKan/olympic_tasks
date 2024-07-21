package pl.mkan.battle_on_chessboard.controller.dto;

public record UnitDTO(Long id, String type, Color color, int x, int y, String status, int moves) {
}
