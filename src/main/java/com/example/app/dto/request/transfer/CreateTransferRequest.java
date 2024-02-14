package com.example.app.dto.request.transfer;

import com.example.app.enums.Currency;
import com.example.app.enums.Status;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
