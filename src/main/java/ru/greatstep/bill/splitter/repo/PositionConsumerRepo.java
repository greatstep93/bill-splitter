package ru.greatstep.bill.splitter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.greatstep.bill.splitter.models.entity.PositionConsumerEntity;

public interface PositionConsumerRepo extends JpaRepository<PositionConsumerEntity, Long> {

    @Modifying
    @Query("DELETE FROM PositionConsumerEntity pe WHERE pe.consumer.id = :consumerId AND pe.position.id = :positionId")
    void deleteByPayerIdAndConsumerId(Long consumerId, Long positionId);
}
