package com.example.app.dto.response.cashbox;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CashboxResponse {
    private Long id;
    private String title;
    private BigDecimal balance;
    private int numberOfTransactions;

    public CashboxResponse(Long id, String title, BigDecimal balance, int numberOfTransactions) {
        this.id = id;
        this.title = title;
        this.balance = balance;
        this.numberOfTransactions = numberOfTransactions;
    }

    public CashboxResponse(String title, BigDecimal balance, int numberOfTransactions) {
        this.title = title;
        this.balance = balance;
        this.numberOfTransactions = numberOfTransactions;
    }
}
