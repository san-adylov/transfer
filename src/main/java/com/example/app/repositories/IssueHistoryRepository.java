package com.example.app.repositories;

import com.example.app.dto.response.transfer.TransferResponse;
import com.example.app.models.IssueHistory;
import com.example.app.models.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IssueHistoryRepository extends JpaRepository<IssueHistory,Long> {
    Optional<IssueHistory> getIssueHistoriesByCodeNumber(String codeNumber);

    @Query("""
        SELECT NEW com.example.app.dto.response.transfer.TransferResponse(
        t.senderFirstName,
        t.senderLastName,
        t.senderSurname,
        t.senderPhoneNumber,
        t.recipientFirstName,
        t.recipientLastName,
        t.recipientSurname,
        t.recipientPhoneNumber,
        i.amountOfMoney,
        i.codeNumber,
        i.createdAt,
        i.issueDate,
        i.comment,
        i.status,
        i.currency,
        t.cashbox.title,
        i.cashbox.title)
        FROM IssueHistory i
        JOIN i.transfer t
        WHERE t.id = ?1 AND t.cashbox.id = ?2
        """)
    TransferResponse getTransferByIdAndCashboxId(Long transferId, Long cashboxId);



}
