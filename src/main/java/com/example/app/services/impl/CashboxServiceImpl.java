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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class CashboxServiceImpl implements CashboxService {

    private final CashboxRepository cashboxRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveCashbox(CreateCashboxRequest request) {
        if (cashboxRepository.existsCashboxByUsername(request.getUsername())){
            throw new BadRequestException("Такой пользователь уже сущевствует: %s ".formatted(request.getUsername()));
        }
        System.out.println(request.getPassword());
        System.out.println(request.getUsername());
        cashboxRepository.save(Cashbox
                .builder()
                        .title(request.getTitle())
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(Role.CASHIER)
                        .balance(new BigDecimal(request.getBalance()))
                .build());
    }

    @Override
    public Page<CashboxResponse> getAllCashbox(int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return cashboxRepository.getAllCashbox(pageable);

    }

    @Override
    public CashboxResponse getCashboxById(Long cashboxId) {
        return cashboxRepository.getCashboxById(cashboxId)
                .orElseThrow(() -> new NotFoundException("Cashbox not found"));
    }

    @Override
    public void updateCashbox(CreateCashboxRequest request) {

    }

    @Override
    public void deleteCashbox(Long cashboxId) {
        Cashbox cashbox = cashboxRepository.findById(cashboxId)
                .orElseThrow(() -> new NotFoundException("Cashbox not found"));
        for (Transfer t : cashbox.getTransfers()){
            if (t.getCashbox().equals(cashbox)) {
                t.setCashbox(null);
            }
        }
        for (IssueHistory i : cashbox.getIssueHistories()){
            if (i.getCashbox().equals(cashbox)) {
                i.setCashbox(null);
            }
        }
        cashboxRepository.delete(cashbox);
    }
}
