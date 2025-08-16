package ru.greatstep.bill.splitter.models.dto.bill;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record PositionDto(
        Long id,
        @NotBlank(message = "Не передан title")
        String title,
        @NotNull(message = "Не передан amount")
        BigDecimal amount,
        List<PayerDto> payers,
        List<ConsumerDto> consumers
) {
}
