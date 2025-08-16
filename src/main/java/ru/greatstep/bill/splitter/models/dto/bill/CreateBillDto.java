package ru.greatstep.bill.splitter.models.dto.bill;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Модель создания нового счета")
public record CreateBillDto(
        @NotBlank(message = "Не передан title")
        @Schema(description = "Название счета", defaultValue = "Новый счет", example = "Какой то новый счет")
        String title
) {
}
