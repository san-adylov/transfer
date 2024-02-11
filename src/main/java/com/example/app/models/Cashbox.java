package com.example.app.models;

import com.example.app.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "cashboxs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cashbox {
    @Id
    @GeneratedValue(generator = "cashbox_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "cashbox_seq", name = "cashbox_gen", allocationSize = 1)
    private Long id;
    private String title;
    private BigDecimal balance;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH},
            mappedBy = "cashbox")
    private List<Transfer> transfers;

}
