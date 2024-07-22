package pl.mkan.battle_on_chessboard.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mkan.battle_on_chessboard.persistence.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

}
