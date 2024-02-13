package com.example.app.services.impl;

import com.example.app.dto.request.transfer.CreateTransferRequest;
import com.example.app.dto.request.transfer.UpdateTransferRequest;
import com.example.app.dto.response.transfer.TransferResponse;
import com.example.app.dto.response.transfer.TransfersResponse;
import com.example.app.enums.Currency;
import com.example.app.enums.Status;
import com.example.app.exceptions.BadRequestException;
import com.example.app.exceptions.ForbiddenException;
import com.example.app.exceptions.NotFoundException;
import com.example.app.models.Cashbox;
import com.example.app.models.IssueHistory;
import com.example.app.models.Transfer;
import com.example.app.repositories.CashboxRepository;
import com.example.app.repositories.IssueHistoryRepository;
import com.example.app.repositories.TransferRepository;
import com.example.app.services.TransferService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final CashboxRepository cashRegisterRepository;
    private final IssueHistoryRepository issueHistoryRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createTransfer(String username, CreateTransferRequest request) {
        validateSenderRecipientPhoneNumbers(request.getSenderPhoneNumber(), request.getRecipientPhoneNumber());

        Cashbox cashRegister = getCashRegisterByUsername(username);

        BigDecimal balanceAmount = salesExchangeRate(request.getAmountOfMoney(), request.getCurrency());

        Transfer transfer = buildTransferEntity(cashRegister, request);
        transferRepository.save(transfer);

        IssueHistory issueHistory = buildIssueHistoryEntity(request, balanceAmount, transfer);
        issueHistoryRepository.save(issueHistory);

        updateCashRegisterBalance(cashRegister, balanceAmount);
    }

    private void validateSenderRecipientPhoneNumbers(String senderPhoneNumber, String recipientPhoneNumber) {
        if (Objects.equals(senderPhoneNumber, recipientPhoneNumber)) {
            throw new BadRequestException("Номер отправителя не должен совпадать с номером получателя!");
        }
    }

    private Cashbox getCashRegisterByUsername(String username) {
        return cashRegisterRepository.getCashRegisterByUsername(username)
                .orElseThrow(() -> new NotFoundException("Не найдено"));
    }


    private Transfer buildTransferEntity(Cashbox cashRegister, CreateTransferRequest request) {
        return Transfer.builder()
                .senderFirstName(request.getSenderFirstName())
                .senderLastName(request.getSenderLastName())
                .senderSurname(request.getSenderSurname())
                .recipientFirstName(request.getRecipientFirstName())
                .recipientLastName(request.getRecipientLastName())
                .recipientSurname(request.getRecipientSurname())
                .cashbox(cashRegister)
                .senderPhoneNumber(request.getSenderPhoneNumber())
                .recipientPhoneNumber(request.getRecipientPhoneNumber())
                .build();
    }

    private IssueHistory buildIssueHistoryEntity(CreateTransferRequest request, BigDecimal balanceAmount, Transfer transfer) {
        UUID uuid = UUID.randomUUID();
        String uniqueCode = uuid.toString();

        return IssueHistory.builder()
                .createdAt(LocalDateTime.now())
                .status(Status.CREATED)
                .currency(request.getCurrency())
                .amountOfMoney(balanceAmount)
                .comment(request.getComment())
                .transfer(transfer)
                .codeNumber(uniqueCode)
                .build();
    }

    private void updateCashRegisterBalance(Cashbox cashRegister, BigDecimal balanceAmount) {
        cashRegister.setBalance(cashRegister.getBalance().add(balanceAmount));
        cashRegisterRepository.save(cashRegister);
    }


    private BigDecimal salesExchangeRate(int summa, Currency currency) {
        Map<String, Double> currencyConversionMap = new HashMap<>();
        currencyConversionMap.put("KGS", 1.0);
        currencyConversionMap.put("USD", 89.43);
        currencyConversionMap.put("KZT", 5.02);
        currencyConversionMap.put("RUB", 0.98);

        double conversionRate = currencyConversionMap.get(currency.name());

        return BigDecimal.valueOf(summa).multiply(BigDecimal.valueOf(conversionRate));
    }

    @Override
    public Page<TransfersResponse> getAllTransfers(String username, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<TransfersResponse> allTransfers = transferRepository.getAllTransfers(pageable);
        for (TransfersResponse transfer : allTransfers) {
            boolean canSeeCode = checkIsSender(username, transfer);
            transfer.setSender(canSeeCode);
        }
        return allTransfers;

    }

    private boolean checkIsSender(String username, TransfersResponse transfersResponse) {
        Cashbox cashRegister = cashRegisterRepository.getCashRegisterByUsername(username)
                .orElseThrow(() -> new NotFoundException("Not found"));
        return cashRegister.getId().equals(transfersResponse.getSenderId());
    }

    @Override
    public TransferResponse getTransfer(String username, Long id) {
        Cashbox cashbox = cashRegisterRepository.getCashRegisterByUsername(username)
                .orElseThrow(() -> new NotFoundException("Not found with username: %s".formatted(username)));
        System.out.printf("""
                Cashbox id %s
                Transfer id %s
                %n""", cashbox.getId(), id);
        boolean b = transferRepository.existsTransferByIdAndCashboxId(id, cashbox.getId());
        if (!b) {
            throw new ForbiddenException("У вас нету доступа");
        }
//        return issueHistoryRepository.getTransferByIdAndCashboxId(id, cashbox.getId());
//                .orElseThrow(() -> new ForbiddenException("У вас нету доступа"));
        return getTransferForTemplate(cashbox.getId(), id);
    }

    private TransferResponse getTransferForTemplate(Long cashboxId, Long transferId) {
        String query = """
                SELECT t.sender_first_name,
                       t.sender_last_name,
                       t.sender_surname,
                       t.sender_phone_number,
                       t.recipient_first_name,
                       t.recipient_last_name,
                       t.recipient_surname,
                       t.recipient_phone_number,
                       ih.amount_of_money,
                       ih.code_number,
                       ih.created_at,
                       ih.issue_date,
                       ih.comment,
                       ih.status,
                       ih.currency,
                       (SELECT c.title
                        FROM cashboxs c
                        WHERE c.id = t.cashbox_id) AS sender_bank_title,
                       (SELECT c.title
                        FROM cashboxs c
                        WHERE c.id = ih.cashbox_id) AS issue_bank_title
                FROM transfers t
                         JOIN issues_histories ih ON t.id = ih.transfer_id
                         JOIN cashboxs c ON t.cashbox_id = c.id
                WHERE t.id = ?
                  AND t.cashbox_id = ?
                """;

        return jdbcTemplate.queryForObject(
                query,
                (rs, rowNum) -> TransferResponse.builder()
                        .senderFirstName(rs.getString("sender_first_name"))
                        .senderLastName(rs.getString("sender_last_name"))
                        .senderSurname(rs.getString("sender_surname"))
                        .senderPhoneNumber(rs.getString("sender_phone_number"))
                        .recipientFirstName(rs.getString("recipient_first_name"))
                        .recipientLastName(rs.getString("recipient_last_name"))
                        .recipientSurname(rs.getString("recipient_surname"))
                        .recipientPhoneNumber(rs.getString("recipient_phone_number"))
                        .amountOfMoney(rs.getString("amount_of_money"))
                        .codeNumber(rs.getString("code_number"))
                        .createdAt(rs.getString("created_at"))
                        .issuedAt(rs.getString("issue_date"))
                        .comment(rs.getString("comment"))
                        .status(rs.getString("status"))
                        .currency(rs.getString("currency"))
                        .senderCashbox(rs.getString("sender_bank_title"))
                        .recipientCashbox(rs.getString("issue_bank_title"))
                        .build(),
                transferId, cashboxId
        );
    }

    @Override
    public void updateTransfer(UpdateTransferRequest request, String username) {
        Cashbox cashbox = cashRegisterRepository.getCashRegisterByUsername(username)
                .orElseThrow(() -> new NotFoundException("Not found with username: %s".formatted(username)));
        IssueHistory issueHistory = issueHistoryRepository.getIssueHistoriesByCodeNumber(request.getCodeNumber())
                .orElseThrow(() -> new NotFoundException("Transfer not found!"));
        Transfer transfer = transferRepository.getTransfer(request.getCodeNumber())
                .orElseThrow(() -> new NotFoundException("Transfer not found"));
        BigDecimal balanceAmount = exchangeRatePurchase(issueHistory.getAmountOfMoney(), request.getCurrency());
        checkRequests(request, transfer, issueHistory);
        validateSufficientFunds(cashbox.getBalance(), balanceAmount);
        saveUpdates(issueHistory, cashbox, transfer);
    }

    private BigDecimal exchangeRatePurchase(BigDecimal summa, Currency currency) {
        Map<String, Double> currencyConversionMap = new HashMap<>();
        currencyConversionMap.put("KGS", 1.0);
        currencyConversionMap.put("USD", 0.011);
        currencyConversionMap.put("KZT", 0.20);
        currencyConversionMap.put("RUB", 1.02);

        double conversionRate = currencyConversionMap.get(currency.name());

        return summa.multiply(BigDecimal.valueOf(conversionRate));
    }

    private void checkRequests(UpdateTransferRequest request, Transfer transfer, IssueHistory issueHistory) {
        if (!transfer.getRecipientFirstName().equals(request.getRecipientFirstName()) &&
                !transfer.getRecipientLastName().equals(request.getRecipientLastName()) &&
                !transfer.getRecipientSurname().equals(request.getRecipientSurname()) &&
                !transfer.getRecipientPhoneNumber().equals(request.getRecipientPhoneNumber()) &&
                !issueHistory.getCodeNumber().equals(request.getCodeNumber())) {
            throw new ForbiddenException("Не правильные данные доступ запрещен");
        }
    }
    private void validateSufficientFunds(BigDecimal cashboxBalance, BigDecimal transferAmount) {
        if (cashboxBalance.compareTo(transferAmount) < 0) {
            throw new BadRequestException("Недостаточно средств");
        }
    }

    private void saveUpdates(IssueHistory issueHistory, Cashbox cashbox, Transfer transfer) {
        issueHistory.setCashbox(cashbox);
        issueHistory.setStatus(Status.ISSUED);
        issueHistory.setIssueDate(LocalDateTime.now());
        cashbox.setBalance(cashbox.getBalance().subtract(issueHistory.getAmountOfMoney()));
        cashRegisterRepository.save(cashbox);
        issueHistoryRepository.save(issueHistory);
        transferRepository.save(transfer);
    }

    @Override
    public void deleteTransfer(Long id) {
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transfer not found"));
        transfer.getCashbox().getTransfers().remove(transfer);
        transferRepository.deleteById(id);
    }
}
