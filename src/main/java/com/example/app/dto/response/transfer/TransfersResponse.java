package com.example.app.dto.response.transfer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor

public class TransfersResponse {
    private Long id;
    private String createTransfer;
    private int amountOfMoney;
    private String senderFullName;
    private String senderPhoneNumber;
    private String recipientFullName;
    private String recipientPhoneNumber;
    private String codeNumber;
    private Long senderId;
    private boolean isSender;

    public TransfersResponse(
            Long id,
            String senderFullName,
            String senderPhoneNumber,
            String recipientFullName,
            String recipientPhoneNumber,
            Long senderId,
            LocalDateTime createTransfer,
            BigDecimal amountOfMoney,
            String codeNumber) {
        this.id = id;
        this.senderFullName = senderFullName;
        this.senderPhoneNumber = senderPhoneNumber;
        this.recipientFullName = recipientFullName;
        this.recipientPhoneNumber = recipientPhoneNumber;
        this.senderId = senderId;
        this.createTransfer = createTransfer.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.amountOfMoney = amountOfMoney.intValue();
        this.codeNumber = codeNumber;

    }
}
