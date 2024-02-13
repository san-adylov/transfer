package com.example.app.services.impl;

import com.example.app.dto.request.auth.SignInRequest;
import com.example.app.dto.response.auth.AuthenticationResponse;
import com.example.app.enums.Role;
import com.example.app.exceptions.NotFoundException;
import com.example.app.models.Cashbox;
import com.example.app.exceptions.BadRequestException;
import com.example.app.repositories.CashboxRepository;
import com.example.app.services.AuthenticationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService {

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
        if (!cashboxRepository.existsCashboxByUsername(cashbox.getUsername())){
            cashboxRepository.save(cashbox);
        }
    }

    @Override
    public AuthenticationResponse singIn(SignInRequest request) {
        Cashbox cashbox = cashboxRepository.getCashRegisterByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("Your account: %s was not found".formatted(request.getPassword())));
        if (!passwordEncoder.matches(request.getPassword(), cashbox.getPassword())) {
            throw new BadRequestException("Incorrect password or username!");
        }
        return AuthenticationResponse
                .builder()
                .build();
    }

}
