package ru.greatstep.bill.splitter.models.dto.bill;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record PayerDto(
        Long id,
        @NotBlank
        String name,
        BigDecimal paid) {
}
