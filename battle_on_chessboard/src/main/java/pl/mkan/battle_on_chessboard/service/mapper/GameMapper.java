package pl.mkan.battle_on_chessboard.service.mapper;

import org.springframework.stereotype.Component;
import pl.mkan.battle_on_chessboard.controller.dto.GameDTO;
import pl.mkan.battle_on_chessboard.controller.dto.UnitDTO;
import pl.mkan.battle_on_chessboard.persistence.model.Game;
import pl.mkan.battle_on_chessboard.persistence.model.Unit;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GameMapper {

    public GameDTO toDto(Game game) {
        List<UnitDTO> units = game.getUnits().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new GameDTO(game.getId(), game.getStatus(), units);
    }

    public UnitDTO toDto(Unit unit) {
        return new UnitDTO(unit.getId(), unit.getType().toString(), unit.getColor(), unit.getX(), unit.getY(), unit.getStatus(), unit.getMoves());
    }
}
