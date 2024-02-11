package com.example.app.models;

import com.example.app.enums.Currency;
import com.example.app.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "issues_histories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueHistory {
    @Id
    @GeneratedValue(generator = "cash_register_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "cash_register_seq", name = "cash_register_gen", allocationSize = 1)
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime issueDate;
    @Column(length = 500)
    private String comment;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private BigDecimal amountOfMoney;
    private String codeNumber;
    @OneToOne(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.DETACH,
                    CascadeType.REFRESH
            })
    private Transfer transfer;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH})
    private Cashbox cashbox;


}
