package ru.greatstep.bill.splitter.repo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.greatstep.bill.splitter.models.entity.UserInfoEntity;

import java.util.Optional;

@Repository
public interface UserInfoRepo extends JpaRepository<UserInfoEntity, Long> {
    Optional<UserInfoEntity> findByEmail(@Email @NotBlank String email);
}
