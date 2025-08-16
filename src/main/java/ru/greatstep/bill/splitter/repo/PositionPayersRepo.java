package ru.greatstep.bill.splitter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.greatstep.bill.splitter.models.entity.PositionPayerEntity;

public interface PositionPayersRepo extends JpaRepository<PositionPayerEntity, Long> {

    @Modifying
    @Query("DELETE FROM PositionPayerEntity pe WHERE pe.payer.id = :payerId AND pe.billPosition.id = :positionId")
    void deleteByPayerIdAndPositionId(Long payerId, Long positionId);

    @Query("FROM PositionPayerEntity pe WHERE pe.payer.id = :payerId AND pe.billPosition.id = :positionId")
    PositionPayerEntity findByPositionIdAndPayerId(Long positionId, Long payerId);
}
