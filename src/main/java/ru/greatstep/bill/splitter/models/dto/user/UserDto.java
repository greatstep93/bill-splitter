package ru.greatstep.bill.splitter.models.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record UserDto(

        @Email(message = "Невалидный email")
        @NotBlank
        String email,

        @NotBlank
        @Length(min = 8, max = 20)
        String password
) {
}
