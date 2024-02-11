package com.example.app.config;

import com.example.app.exceptions.NotFoundException;
import com.example.app.models.Cashbox;
import com.example.app.repositories.CashboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetailsService {

    private final CashboxRepository cashboxRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cashbox cashbox = cashboxRepository.getCashRegisterByUsername(username)
                .orElseThrow(() -> new NotFoundException("Cashier with username: not found"));
        if (cashbox == null) {
            return null;
        }
        return User.builder()
                .username(cashbox.getUsername())
                .password(cashbox.getPassword())
                .roles(cashbox.getRole().name())
                .build();
    }
}
