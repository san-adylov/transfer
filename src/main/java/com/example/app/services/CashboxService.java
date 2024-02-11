package com.example.app.services;

import com.example.app.dto.request.cahsbox.CreateCashboxRequest;
import com.example.app.dto.response.cashbox.CashboxResponse;
import org.springframework.data.domain.Page;

public interface CashboxService {

    void saveCashbox(CreateCashboxRequest request);

    Page<CashboxResponse> getAllCashbox();

    void updateCashbox(CreateCashboxRequest request);

    void deleteCashbox(String username);


}
