package ru.greatstep.bill.splitter.models.dto.bill;

import java.math.BigDecimal;

public record ConsumerDto(
        String name,
        BigDecimal debt
) {
}
