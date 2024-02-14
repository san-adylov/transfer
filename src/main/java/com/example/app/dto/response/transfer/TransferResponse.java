package com.example.app.dto.response.transfer;

import com.example.app.enums.Currency;
import com.example.app.enums.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {

  private String senderFirstName;
  private String senderLastName;
  private String senderSurname;
  private String senderPhoneNumber;
  private String recipientFirstName;
  private String recipientLastName;
  private String recipientSurname;
  private String recipientPhoneNumber;
  private String amountOfMoney;
  private String codeNumber;
  private String createdAt;
  private String comment;
  private String status;
  private String currency;
  private String senderCashbox;

  public TransferResponse(
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
      Currency currency,
      String senderCashbox) {
    this.senderFirstName = senderFirstName;
    this.senderLastName = senderLastName;
    this.senderSurname = senderSurname;
    this.senderPhoneNumber = senderPhoneNumber;
    this.recipientFirstName = recipientFirstName;
    this.recipientLastName = recipientLastName;
    this.recipientSurname = recipientSurname;
    this.recipientPhoneNumber = recipientPhoneNumber;
    this.amountOfMoney = amountOfMoney.toString();
    this.codeNumber = codeNumber;
    this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    this.comment = comment;
    this.status = status.name();
    this.currency = currency.name();
    this.senderCashbox = senderCashbox;
  }
}
