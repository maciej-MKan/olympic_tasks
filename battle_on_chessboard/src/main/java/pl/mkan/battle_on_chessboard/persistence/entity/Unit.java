package pl.mkan.battle_on_chessboard.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.mkan.battle_on_chessboard.controller.dto.Color;

@Entity
@Getter
@Setter
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private UnitType type;
    @Enumerated(EnumType.STRING)
    private Color color;
    private int x;
    private int y;
    private String status;
    private int moves;

    @Version
    private int version;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
}

