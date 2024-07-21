package pl.mkan.battle_on_chessboard.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mkan.battle_on_chessboard.controller.dto.Color;
import pl.mkan.battle_on_chessboard.persistence.entity.Unit;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    List<Unit> findByGameIdAndColor(Long gameId, Color color);

    Optional<Unit> findByIdAndGameIdAndColor(Long unitId, Long gameId, Color color);
}
