package ru.greatstep.bill.splitter.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.greatstep.bill.splitter.models.entity.BillDataEntity;

import java.util.List;

public interface BillsRepo extends JpaRepository<BillDataEntity, Long> {

    @Query("FROM BillDataEntity b WHERE b.createUser.email = :email")
    List<BillDataEntity> findByUserEmail(String email);
}
