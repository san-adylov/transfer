package com.example.app.dto.request.transfer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateTransferRequest {
    private String recipientFirstName;
    private String recipientLastName;
    private String recipientSurname;
    private String recipientPhoneNumber;
    private String codeNumber;
}
