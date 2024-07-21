package pl.mkan.battle_on_chessboard.controller.dto;

import java.util.List;

public record GameDTO(Long id, String status, List<UnitDTO> units) {
}
