package com.example.app.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transfers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transfer {

  @Id
  @GeneratedValue(generator = "transfer_gen", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(sequenceName = "transfer_seq", name = "transfer_gen", allocationSize = 1)
  private Long id;
  private String senderFirstName;
  private String senderLastName;
  private String senderSurname;
  private String senderPhoneNumber;
  private String recipientFirstName;
  private String recipientLastName;
  private String recipientSurname;
  private String recipientPhoneNumber;
  @ManyToOne(cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE,
      CascadeType.DETACH,
      CascadeType.REFRESH})
  private Cashbox cashbox;
  @OneToOne(
      cascade = CascadeType.ALL,
      mappedBy = "transfer"
  )
  private IssueHistory issueHistory;
}
