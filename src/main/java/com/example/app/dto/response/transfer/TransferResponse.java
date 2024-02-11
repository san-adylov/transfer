package com.example.app.dto.response.transfer;

import com.example.app.enums.Currency;
import com.example.app.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponse(
        String senderFirstName,
        String senderLastName,
        String senderSurname,
        String senderPhoneNumber,
        String recipientFirstName,
        String recipientLastName,
        String recipientSurname,
        String recipientPhoneNumber,
        BigDecimal amountOfMoney,
        String codeNumber,
        LocalDateTime createdAt,
        String comment,
        Status status,
        Currency currency
) {
}
