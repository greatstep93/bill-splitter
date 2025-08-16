package ru.greatstep.bill.splitter.models.dto.bill;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record BillDto(
        Long id,
        @NotBlank(message = "Не передан title")
        String title,
        LocalDate date,
        String creatorName,
        List<PositionDto> positions,
        List<ConsumerDto> deptSummary,
        List<PayerDto> allPayers,
        BigDecimal sum
) {
}
