package com.example.app.repositories;

import com.example.app.models.Cashbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CashboxRepository extends JpaRepository<Cashbox, Long> {

    Optional<Cashbox> getCashRegisterByUsername(String username);

    boolean existsCashboxByUsername(String username);
}
