package ru.greatstep.bill.splitter.service.bills;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.greatstep.bill.splitter.models.dto.bill.PositionDto;
import ru.greatstep.bill.splitter.models.entity.BillPositionEntity;
import ru.greatstep.bill.splitter.repo.BillPositionRepo;
import ru.greatstep.bill.splitter.repo.BillsRepo;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final BillsRepo billsRepo;
    private final BillPositionRepo billPositionRepo;
    private final BillsService billsService;

    public PositionDto createPosition(String authHeader, Long billId, PositionDto dto) {
        var bill = billsRepo.findById(billId).orElseThrow(() -> new RuntimeException("Счет не найден"));
        billsService.checkBill(authHeader, bill);
        var billPositionEntity = new BillPositionEntity();

        billPositionEntity.setTitle(dto.title());
        billPositionEntity.setBill(bill);
        billPositionEntity.setAmount(dto.amount());

        var saved = billPositionRepo.save(billPositionEntity);
        return new PositionDto(saved.getId(), saved.getTitle(), saved.getAmount(), null, null);
    }

    public String deletePosition(String authHeader, Long id) {
        var position = billPositionRepo.findById(id).orElseThrow(() -> new RuntimeException("Position not found"));
        billsService.checkBill(authHeader, position.getBill());
        billPositionRepo.delete(position);
        return "Delete position successfully";
    }

    public PositionDto updatePosition(String authHeader, Long id, PositionDto dto) {
        var position = billPositionRepo.findById(id).orElseThrow(() -> new RuntimeException("Position not found"));
        billsService.checkBill(authHeader, position.getBill());
        var isChanged = false;
        if (isNotBlank(dto.title())) {
            position.setTitle(dto.title());
            isChanged = true;
        }

        if (dto.amount() != null) {
            position.setAmount(dto.amount());
            isChanged = true;
        }

        if (isChanged) {
            position = billPositionRepo.save(position);
        }

        return new PositionDto(
                position.getId(),
                position.getTitle(),
                position.getAmount(),
                billsService.getPayers(position.getPositionPayers()),
                billsService.getConsumers(position.getPositionConsumers())
        );
    }
}
