package ru.greatstep.bill.splitter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.greatstep.bill.splitter.models.entity.PayerEntity;

public interface PayerRepo extends JpaRepository<PayerEntity, Long> {
}
