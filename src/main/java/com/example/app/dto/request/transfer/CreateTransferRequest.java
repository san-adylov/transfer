package com.example.app.dto.request.transfer;

import com.example.app.enums.Currency;
import com.example.app.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateTransferRequest {
    private String senderFirstName;
    private String senderLastName;
    private String senderSurname;
    private String senderPhoneNumber;
    private String recipientFirstName;
    private String recipientLastName;
    private String recipientSurname;
    private String recipientPhoneNumber;
    private int amountOfMoney;
    private String codeNumber;
    private LocalDateTime createdAt;
    private String comment;
    private Status status;
    private Currency currency;
}
