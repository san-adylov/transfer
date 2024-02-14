package com.example.app.dto.request.transfer;

import com.example.app.enums.Currency;
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
  private Currency currency;
}
