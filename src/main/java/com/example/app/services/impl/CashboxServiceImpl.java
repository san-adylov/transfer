package com.example.app.services.impl;

import com.example.app.dto.request.cahsbox.CreateCashboxRequest;
import com.example.app.dto.response.cashbox.CashboxResponse;
import com.example.app.services.CashboxService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CashboxServiceImpl implements CashboxService {
    @Override
    public void saveCashbox(CreateCashboxRequest request) {

    }

    @Override
    public Page<CashboxResponse> getAllCashbox() {
        return null;
    }

    @Override
    public void updateCashbox(CreateCashboxRequest request) {

    }

    @Override
    public void deleteCashbox(String username) {

    }
}
