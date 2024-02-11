package com.example.app.dto.request.cahsbox;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateCashboxRequest {
    private String title;
    private String username;
    private String password;
    private int balance;
}
