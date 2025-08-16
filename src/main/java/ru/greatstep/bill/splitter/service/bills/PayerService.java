package ru.greatstep.bill.splitter.service.bills;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.greatstep.bill.splitter.models.dto.bill.PayerDto;
import ru.greatstep.bill.splitter.models.dto.bill.PositionDto;
import ru.greatstep.bill.splitter.models.entity.PayerEntity;
import ru.greatstep.bill.splitter.models.entity.PositionConsumerEntity;
import ru.greatstep.bill.splitter.models.entity.PositionPayerEntity;
import ru.greatstep.bill.splitter.repo.BillPositionRepo;
import ru.greatstep.bill.splitter.repo.BillsRepo;
import ru.greatstep.bill.splitter.repo.PayerRepo;
import ru.greatstep.bill.splitter.repo.PositionConsumerRepo;
import ru.greatstep.bill.splitter.repo.PositionPayersRepo;

import java.math.BigDecimal;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class PayerService {

    private final BillsRepo billsRepo;
    private final BillsService billsService;
    private final PayerRepo payerRepo;
    private final BillPositionRepo billPositionRepo;
    private final PositionPayersRepo positionPayersRepo;
    private final PositionConsumerRepo positionConsumerRepo;

    public PayerDto createPayer(String authHeader, Long billId, PayerDto dto) {
        var bill = billsRepo.findById(billId).orElseThrow(() -> new RuntimeException("Счет не найден"));
        billsService.checkBill(authHeader, bill);
        PayerEntity payer = new PayerEntity();
        payer.setFullName(dto.name());
        payer.setCreatedUser(bill.getCreateUser());
        payer.setBillData(bill);
        var savedPayer = payerRepo.save(payer);
        return new PayerDto(savedPayer.getId(), savedPayer.getFullName(), null);
    }

    public PositionDto addPayerForPosition(String authHeader, Long positionId, Long payerId, BigDecimal amount) {
        var position = billPositionRepo.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Позиция не найдена"));
        billsService.checkBill(authHeader, position.getBill());
        var payer = payerRepo.findById(payerId).orElseThrow(() -> new RuntimeException("Плательщик не найден"));
        var positionPayer = new PositionPayerEntity();
        positionPayer.setPayer(payer);
        positionPayer.setBillPosition(position);
        positionPayer.setAmount(amount);
        positionPayersRepo.save(positionPayer);
        return new PositionDto(position.getId(),
                position.getTitle(),
                position.getAmount(),
                billsService.getPayers(position.getPositionPayers()),
                billsService.getConsumers(position.getPositionConsumers())
        );
    }

    public PositionDto addConsumerForPosition(String authHeader, Long positionId, Long consumerId) {
        var position = billPositionRepo.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Позиция не найдена"));
        billsService.checkBill(authHeader, position.getBill());
        var payer = payerRepo.findById(consumerId).orElseThrow(() -> new RuntimeException("Плательщик не найден"));
        var positionConsumer = new PositionConsumerEntity();
        positionConsumer.setBill(position.getBill());
        positionConsumer.setPosition(position);
        positionConsumer.setConsumer(payer);
        positionConsumerRepo.save(positionConsumer);
        return new PositionDto(position.getId(),
                position.getTitle(),
                position.getAmount(),
                billsService.getPayers(position.getPositionPayers()),
                billsService.getConsumers(position.getPositionConsumers())
        );
    }

    public String deletePayer(String authHeader, Long id) {
        var payer = payerRepo.findById(id).orElseThrow(() -> new RuntimeException("Payer not found"));
        billsService.checkBill(authHeader, payer.getBillData());
        payerRepo.delete(payer);
        return "Delete payer successfully";
    }

    public String dropPayerForPosition(String authHeader, Long positionId, Long payerId) {
        var position = billPositionRepo.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        billsService.checkBill(authHeader, position.getBill());
        positionPayersRepo.deleteByPayerIdAndPositionId(payerId, positionId);
        return "Delete payer for position successfully";
    }

    public String dropConsumerForPosition(String authHeader, Long positionId, Long consumerId) {
        var position = billPositionRepo.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        billsService.checkBill(authHeader, position.getBill());
        positionConsumerRepo.deleteByPayerIdAndConsumerId(consumerId, positionId);
        return "Delete payer for position successfully";
    }

    public PayerDto updatePayer(String authHeader, Long id, PayerDto dto) {
        var payer = payerRepo.findById(id).orElseThrow(() -> new RuntimeException("Payer not found"));
        billsService.checkBill(authHeader, payer.getBillData());
        if (isNotBlank(dto.name())) {
            payer.setFullName(dto.name());
            payer = payerRepo.save(payer);
        }
        return new PayerDto(
                payer.getId(),
                payer.getFullName(),
                null
        );
    }

    public PositionDto updatePayerForPosition(String authHeader, Long positionId, Long payerId, BigDecimal amount) {
        var position = billPositionRepo.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Позиция не найдена"));
        billsService.checkBill(authHeader, position.getBill());
        payerRepo.findById(payerId).orElseThrow(() -> new RuntimeException("Плательщик не найден"));
        var positionPayer = positionPayersRepo.findByPositionIdAndPayerId(positionId, payerId);
        positionPayer.setAmount(amount);
        var saved = positionPayersRepo.save(positionPayer);
        return new PositionDto(saved.getBillPosition().getId(),
                saved.getBillPosition().getTitle(),
                saved.getBillPosition().getAmount(),
                billsService.getPayers(saved.getBillPosition().getPositionPayers()),
                billsService.getConsumers(saved.getBillPosition().getPositionConsumers())
        );
    }
}
