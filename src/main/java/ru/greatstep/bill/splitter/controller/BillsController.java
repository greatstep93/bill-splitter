package ru.greatstep.bill.splitter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.greatstep.bill.splitter.models.dto.bill.BillDto;
import ru.greatstep.bill.splitter.models.dto.bill.CreateBillDto;
import ru.greatstep.bill.splitter.service.bills.BillsService;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@Tag(name = "BillsController", description = "CRUD операции со счетами пользователя")
public class BillsController {

    private final BillsService billsService;

    @GetMapping("/bills")
    @Operation(summary = "Получить все счета пользователя")
    public List<BillDto> getAllDays(@RequestHeader(AUTHORIZATION) String authHeader) {
        return billsService.getBillsForUser(authHeader);
    }

    @PostMapping("/createBill")
    @Operation(summary = "Создать новый счет от имени авторизованного пользователя")
    public BillDto addBill(
            @RequestHeader(AUTHORIZATION) String authHeader,
            @RequestBody @Valid CreateBillDto dto
    ) {
        return billsService.createNewBill(authHeader, dto);
    }

    @DeleteMapping("/bill/{id}")
    @Operation(summary = "Удалить счет по его id")
    public String deleteBill(
            @RequestHeader(AUTHORIZATION) String authHeader,
            @PathVariable("id") Long id
    ) {
        return billsService.deleteBill(authHeader, id);
    }

    @PatchMapping("/bill/{id}")
    @Operation(summary = "Обновить счет по его id")
    public BillDto updateBill(
            @RequestHeader(AUTHORIZATION) String authHeader,
            @RequestBody BillDto dto,
            @PathVariable("id") Long id
    ) {
        return billsService.updateBill(authHeader, dto, id);
    }
}
