package ru.greatstep.bill.splitter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.greatstep.bill.splitter.models.dto.bill.PayerDto;
import ru.greatstep.bill.splitter.models.dto.bill.PositionDto;
import ru.greatstep.bill.splitter.service.bills.PayerService;

import java.math.BigDecimal;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
public class PayerController {

    private final PayerService payerService;

    @PostMapping("/{billId}/addPayer")
    public PayerDto addPayer(
            @RequestHeader(AUTHORIZATION) String authHeader,
            @PathVariable("billId") Long billId,
            @RequestBody @Valid PayerDto dto
    ) {
        return payerService.createPayer(authHeader, billId, dto);
    }

    @PatchMapping("/payer/{id}")
    public PayerDto deletePayer(
            @RequestHeader(AUTHORIZATION) String authHeader,
            @PathVariable("id") Long id,
            @RequestBody PayerDto dto
    ) {
        return payerService.updatePayer(authHeader, id, dto);
    }

    @DeleteMapping("/payer/{id}")
    public String deletePayer(
            @RequestHeader(AUTHORIZATION) String authHeader,
            @PathVariable("id") Long id
    ) {
        return payerService.deletePayer(authHeader, id);
    }

    @PostMapping("/addPayerForPosition")
    public PositionDto addPayerForPosition(
            @RequestHeader(AUTHORIZATION) String authHeader,
            @RequestParam("positionId") Long positionId,
            @RequestParam("payerId") Long payerId,
            @RequestParam("amount") BigDecimal amount
    ) {
        return payerService.addPayerForPosition(authHeader, positionId, payerId, amount);
    }

    @PatchMapping("/payerForPosition")
    public PositionDto updatePayerForPosition(
            @RequestHeader(AUTHORIZATION) String authHeader,
            @RequestParam("positionId") Long positionId,
            @RequestParam("payerId") Long payerId,
            @RequestParam("amount") BigDecimal amount
    ) {
        return payerService.updatePayerForPosition(authHeader, positionId, payerId, amount);
    }

    @DeleteMapping("dropPayerForPosition")
    public String dropPayerForPosition(
            @RequestHeader(AUTHORIZATION) String authHeader,
            @RequestParam("positionId") Long positionId,
            @RequestParam("payerId") Long payerId
    ) {
        return payerService.dropPayerForPosition(authHeader, positionId, payerId);
    }

    @PostMapping("/addConsumerForPosition")
    public PositionDto addConsumerForPosition(
            @RequestHeader(AUTHORIZATION) String authHeader,
            @RequestParam("positionId") Long positionId,
            @RequestParam("consumerId") Long consumerId
    ) {
        return payerService.addConsumerForPosition(authHeader, positionId, consumerId);
    }

    @DeleteMapping("/dropConsumerForPosition")
    public String dropConsumerForPosition(
            @RequestHeader(AUTHORIZATION) String authHeader,
            @RequestParam("positionId") Long positionId,
            @RequestParam("consumerId") Long consumerId
    ) {
        return payerService.dropConsumerForPosition(authHeader, positionId, consumerId);
    }
}
