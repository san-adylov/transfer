package com.example.app.models;

import com.example.app.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
  @OneToMany(cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE,
      CascadeType.DETACH,
      CascadeType.REFRESH},
      mappedBy = "cashbox")
  private List<IssueHistory> issueHistories;

}
