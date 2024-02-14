package com.example.app.services.impl;

import com.example.app.dto.request.cahsbox.CreateCashboxRequest;
import com.example.app.dto.response.cashbox.CashboxResponse;
import com.example.app.enums.Role;
import com.example.app.exceptions.BadRequestException;
import com.example.app.exceptions.NotFoundException;
import com.example.app.models.Cashbox;
import com.example.app.models.IssueHistory;
import com.example.app.models.Transfer;
import com.example.app.repositories.CashboxRepository;
import com.example.app.services.CashboxService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CashboxServiceImpl implements CashboxService {

  private final CashboxRepository cashboxRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void saveCashbox(CreateCashboxRequest request) {
    if (cashboxRepository.existsCashboxByUsername(request.getUsername())) {
      log.error("Error: This user already exists");
      throw new BadRequestException(
          "Такой пользователь уже сущевствует: %s ".formatted(request.getUsername()));
    }
    cashboxRepository.save(Cashbox
        .builder()
        .title(request.getTitle())
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.CASHIER)
        .balance(new BigDecimal(request.getBalance()))
        .build());
    log.info("save cashbox");
  }

  @Override
  public Page<CashboxResponse> getAllCashbox(int page) {
    Pageable pageable = PageRequest.of(page, 5);
    log.info("get all cashbox");
    return cashboxRepository.getAllCashbox(pageable);

  }

  @Override
  public CashboxResponse getCashboxById(Long cashboxId) {
    log.info("get cashbox by id");
    return cashboxRepository.getCashboxById(cashboxId)
        .orElseThrow(() -> {
          log.error("Cashbox not found");
          return new NotFoundException("Cashbox not found");
        });
  }

  @Override
  public void updateCashbox(CreateCashboxRequest request, Long cashboxId) {
    Cashbox cashbox = cashboxRepository.findById(cashboxId)
        .orElseThrow(() -> {
          log.error("Error: cashbox not found");
          return new NotFoundException("Касса не найдена");
        });
    cashbox.setTitle(
        request.getTitle() == null || request.getTitle().isBlank() || cashbox.getTitle()
            .equalsIgnoreCase(request.getTitle()) ? cashbox.getTitle() : request.getTitle());
    cashbox.setBalance(
        request.getBalance() <= 0 || cashbox.getBalance().intValue() == request.getBalance()
            ? cashbox.getBalance() : new BigDecimal(request.getBalance()));
    cashboxRepository.save(cashbox);
    log.info("cashbox updated");
  }

  @Override
  public void deleteCashboxById(Long cashboxId) {
    Cashbox cashbox = cashboxRepository.findById(cashboxId)
        .orElseThrow(() -> {
          log.error("Error: cashbox not found");
          return new NotFoundException("Касса не найдена");
        });
    for (Transfer t : cashbox.getTransfers()) {
      if (t.getCashbox().equals(cashbox)) {
        t.setCashbox(null);
      }
    }
    for (IssueHistory i : cashbox.getIssueHistories()) {
      if (i.getCashbox().equals(cashbox)) {
        i.setCashbox(null);
      }
    }
    cashboxRepository.delete(cashbox);
    log.info("cashbox by id deleted");
  }
}
