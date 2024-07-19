package pl.mkan.battle_on_chessboard.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mkan.battle_on_chessboard.persistence.entity.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
}
