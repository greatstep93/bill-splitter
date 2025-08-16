package ru.greatstep.bill.splitter.service.bills;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.greatstep.bill.splitter.models.dto.bill.BillDto;
import ru.greatstep.bill.splitter.models.dto.bill.ConsumerDto;
import ru.greatstep.bill.splitter.models.dto.bill.CreateBillDto;
import ru.greatstep.bill.splitter.models.dto.bill.PayerDto;
import ru.greatstep.bill.splitter.models.dto.bill.PositionDto;
import ru.greatstep.bill.splitter.models.entity.BillDataEntity;
import ru.greatstep.bill.splitter.models.entity.BillPositionEntity;
import ru.greatstep.bill.splitter.models.entity.PayerEntity;
import ru.greatstep.bill.splitter.models.entity.PositionConsumerEntity;
import ru.greatstep.bill.splitter.models.entity.PositionPayerEntity;
import ru.greatstep.bill.splitter.repo.BillsRepo;
import ru.greatstep.bill.splitter.repo.UserInfoRepo;
import ru.greatstep.bill.splitter.service.user.JwtService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.math.RoundingMode.HALF_EVEN;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class BillsService {

    private final BillsRepo billsRepo;
    private final JwtService jwtService;
    private final UserInfoRepo userInfoRepo;

    public List<BillDto> getBillsForUser(String authHeader) {
        var username = jwtService.extractUsernameFromHeader(authHeader);
        var billsList = billsRepo.findByUserEmail(username);

        return billsList.stream()
                .map(b -> {
                    var positions = getPositions(b.getBillPositions());
                    return new BillDto(
                            b.getId(),
                            b.getTitle(),
                            b.getDate(),
                            b.getCreateUser().getUsername(),
                            positions,
                            getDeptSummary(positions),
                            getPayersDto(b.getPayers()),
                            sum(b.getBillPositions())
                    );
                })
                .toList();
    }


    public BillDto createNewBill(String authHeader, CreateBillDto dto) {
        var username = jwtService.extractUsernameFromHeader(authHeader);
        var user = userInfoRepo.findByEmail(username).orElseThrow();
        BillDataEntity billDataEntity = new BillDataEntity();
        billDataEntity.setTitle(dto.title());
        billDataEntity.setCreateUser(user);
        var saved = billsRepo.save(billDataEntity);
        return new BillDto(
                saved.getId(),
                saved.getTitle(),
                saved.getDate(),
                saved.getCreateUser().getEmail(),
                null,
                null,
                null,
                BigDecimal.ZERO
        );
    }


    public String deleteBill(String authHeader, Long id) {
        var bill = billsRepo.findById(id).orElseThrow(() -> new RuntimeException("Bill not found"));
        checkBill(authHeader, bill);
        billsRepo.delete(bill);
        return "Delete bill success";
    }

    public List<ConsumerDto> getDeptSummary(List<PositionDto> positions) {
        Map<String, BigDecimal> paymentSums = new HashMap<>();
        positions.forEach(position -> position.payers()
                .forEach(p -> paymentSums.put(p.name(), getNewValue(paymentSums, p))));

        Map<String, BigDecimal> deptSums = new HashMap<>();
        positions.forEach(position -> position.consumers()
                .forEach(c -> deptSums.put(c.name(), getNewValue(deptSums, c))));

        return deptSums.entrySet().stream()
                .map(e -> new ConsumerDto(e.getKey(), getNewValue(paymentSums, e)))
                .toList();
    }

    public BigDecimal getNewValue(Map<String, BigDecimal> paymentSums, Map.Entry<String, BigDecimal> dept) {
        return paymentSums.containsKey(dept.getKey())
                ? dept.getValue().subtract(paymentSums.get(dept.getKey()))
                : dept.getValue();
    }

    public BigDecimal getNewValue(Map<String, BigDecimal> paymentSums, ConsumerDto p) {
        return paymentSums.containsKey(p.name())
                ? paymentSums.get(p.name()).add(p.debt())
                : p.debt();
    }

    public BigDecimal getNewValue(Map<String, BigDecimal> paymentSums, PayerDto p) {
        return paymentSums.containsKey(p.name())
                ? paymentSums.get(p.name()).add(p.paid())
                : p.paid();
    }


    public List<PositionDto> getPositions(Set<BillPositionEntity> billPositions) {
        return billPositions.stream()
                .map(p -> new PositionDto(
                        p.getId(),
                        p.getTitle(),
                        p.getAmount(),
                        getPayers(p.getPositionPayers()),
                        getConsumers(p.getPositionConsumers())
                ))
                .toList();
    }

    public List<ConsumerDto> getConsumers(Set<PositionConsumerEntity> positionConsumers) {
        if (positionConsumers == null || positionConsumers.isEmpty()) {
            return Collections.emptyList();
        }
        return positionConsumers.stream()
                .map(pc -> new ConsumerDto(
                        pc.getConsumer().getFullName(),
                        pc.getPosition().getAmount().divide(BigDecimal.valueOf(positionConsumers.size()), 2, HALF_EVEN)
                ))
                .toList();
    }

    public List<PayerDto> getPayersDto(Set<PayerEntity> payers) {
        return payers.stream()
                .map(p -> new PayerDto(
                                p.getId(),
                                p.getFullName(),
                                null
                        )
                )
                .toList();
    }

    public List<PayerDto> getPayers(Set<PositionPayerEntity> payers) {
        return payers.stream()
                .map(p -> new PayerDto(
                                p.getPayer().getId(),
                                p.getPayer().getFullName(),
                                p.getAmount()
                        )
                )
                .toList();
    }

    public BigDecimal sum(Set<BillPositionEntity> billPositions) {
        BigDecimal sum = BigDecimal.ZERO;
        return billPositions.stream()
                .map(BillPositionEntity::getAmount)
                .reduce(sum, BigDecimal::add);
    }

    public void checkBill(String authHeader, BillDataEntity bill) {
        var username = jwtService.extractUsernameFromHeader(authHeader);
        if (!bill.getCreateUser().getUsername().equals(username)) {
            throw new RuntimeException("У вас нет прав на редактирование этого счета");
        }
    }

    public BillDto updateBill(String authHeader, BillDto dto, Long id) {
        var bill = billsRepo.findById(id).orElseThrow(() -> new RuntimeException("Bill not found"));
        checkBill(authHeader, bill);
        boolean isChanged = false;
        if (isNotBlank(dto.title())) {
            bill.setTitle(dto.title());
            isChanged = true;
        }

        if (nonNull(dto.date())) {
            bill.setDate(dto.date());
            isChanged = true;
        }

        if (isChanged) {
            bill = billsRepo.save(bill);
        }

        var positions = getPositions(bill.getBillPositions());
        return new BillDto(
                bill.getId(),
                bill.getTitle(),
                bill.getDate(),
                bill.getCreateUser().getUsername(),
                positions,
                getDeptSummary(positions),
                getPayersDto(bill.getPayers()),
                sum(bill.getBillPositions()));
    }
}
