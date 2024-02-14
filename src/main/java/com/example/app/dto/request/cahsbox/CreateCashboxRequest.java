package com.example.app.dto.request.cahsbox;

import com.example.app.annotations.password.Password;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCashboxRequest {

  private String title;
  private String username;
  @Password(message = "Password incorrect")
  private String password;
  private int balance;
}
