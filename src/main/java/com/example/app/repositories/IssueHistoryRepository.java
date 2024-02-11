package com.example.app.repositories;

import com.example.app.models.IssueHistory;
import com.example.app.models.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IssueHistoryRepository extends JpaRepository<IssueHistory,Long> {
//    @Query("SELECT ih FROM IssueHistory ih WHERE ih.codeNumber = ?1 AND ih.status = 'ISSUED'")
    Optional<IssueHistory> getIssueHistoriesByCodeNumber(String codeNumber);


}
