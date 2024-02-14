package com.example.app.repositories;

import com.example.app.dto.response.transfer.TransfersResponse;
import com.example.app.models.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("SELECT NEW com.example.app.dto.response.transfer.TransfersResponse(" +
        "t.id," +
        "CONCAT(t.senderFirstName, ' ', t.senderLastName), " +
        "t.senderPhoneNumber, " +
        "CONCAT(t.recipientFirstName, ' ', t.recipientLastName), " +
        "t.recipientPhoneNumber, " +
        "t.cashbox.id, " +
        "ih.createdAt," +
        "ih.amountOfMoney," +
        "ih.codeNumber" +
        ") " +
        "FROM Transfer t JOIN IssueHistory ih ON t.issueHistory.id = ih.id")
    Page<TransfersResponse> getAllTransfers(Pageable pageable);

    @Query("""
            SELECT t FROM Transfer t
            JOIN IssueHistory ih ON t.issueHistory.id = ih.id
            WHERE ih.codeNumber = ?1
            """)
    Optional<Transfer> getTransfer(String codeNumber);

    boolean existsTransferByIdAndCashboxId(Long transferId, Long cashboxId);

}
