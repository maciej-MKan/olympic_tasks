package pl.mkan.battle_on_chessboard.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import pl.mkan.battle_on_chessboard.controller.dto.Color;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class CommandHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long gameId;
    private Long unitId;
    private Color color;
    private String command;
    private String direction;
    private int distance;
    private LocalDateTime commandTime;
}
