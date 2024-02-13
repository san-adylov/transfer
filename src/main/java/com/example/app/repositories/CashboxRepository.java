package com.example.app.repositories;

import com.example.app.dto.response.cashbox.CashboxResponse;
import com.example.app.models.Cashbox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CashboxRepository extends JpaRepository<Cashbox, Long> {

    Optional<Cashbox> getCashRegisterByUsername(String username);

    boolean existsCashboxByUsername(String username);

    boolean existsByUsername(String username);

    @Query("""
            SELECT NEW com.example.app.dto.response.cashbox.CashboxResponse(
            c.id,
            c.title,
            c.balance,
            CAST(COUNT(t.id) AS int))
            FROM Cashbox c
            LEFT JOIN c.transfers t
            WHERE c.role = 'CASHIER'
            GROUP BY c.id, c.title,c.balance
            """)
    Page<CashboxResponse> getAllCashbox(Pageable pageable);

    @Query("""
            SELECT NEW com.example.app.dto.response.cashbox.CashboxResponse(
            c.title,
            c.balance,
            CAST(COUNT(t.id) AS int))
            FROM Cashbox c
            LEFT JOIN c.transfers t
            WHERE c.id = ?1
            GROUP BY c.title,c.balance
            """)
    Optional<CashboxResponse> getCashboxById (Long cashboxId);


}
