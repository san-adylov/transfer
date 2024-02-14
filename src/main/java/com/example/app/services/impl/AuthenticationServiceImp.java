package com.example.app.services.impl;

import com.example.app.enums.Role;
import com.example.app.models.Cashbox;
import com.example.app.repositories.CashboxRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp {

  private final CashboxRepository cashboxRepository;
  private final PasswordEncoder passwordEncoder;

  @PostConstruct
  private void addAdmin() {
    Cashbox cashbox = Cashbox
        .builder()
        .title("admin")
        .role(Role.ADMIN)
        .username("Sanzhar")
        .password(passwordEncoder.encode("Sanzhar"))
        .build();
    if (!cashboxRepository.existsCashboxByUsername(cashbox.getUsername())) {
      cashboxRepository.save(cashbox);
      log.info("admin save");
    }
  }

}
