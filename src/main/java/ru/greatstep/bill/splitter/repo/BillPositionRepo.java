package ru.greatstep.bill.splitter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.greatstep.bill.splitter.models.entity.BillPositionEntity;

public interface BillPositionRepo extends JpaRepository<BillPositionEntity, Long> {
}
