package com.example.app.dto.response.cashbox;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashboxResponse {
    private String title;
    private double balance;
}
