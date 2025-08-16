package ru.greatstep.bill.splitter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.greatstep.bill.splitter.models.dto.bill.PositionDto;
import ru.greatstep.bill.splitter.service.bills.PositionService;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @PostMapping("/{billId}/addPosition")
    public PositionDto addPosition(
            @RequestHeader(AUTHORIZATION) String authHeader,
            @PathVariable("billId") Long billId,
            @RequestBody @Valid PositionDto dto
    ) {
        return positionService.createPosition(authHeader, billId, dto);
    }

    @PatchMapping("/position/{id}")
    public PositionDto updatePosition(
            @RequestHeader(AUTHORIZATION) String authHeader,
            @PathVariable("id") Long id,
            @RequestBody PositionDto dto
    ) {
        return positionService.updatePosition(authHeader, id, dto);
    }

    @DeleteMapping("/position/{id}")
    public String dropPosition(
            @RequestHeader(AUTHORIZATION) String authHeader,
            @PathVariable("id") Long id
    ) {
        return positionService.deletePosition(authHeader, id);
    }
}
