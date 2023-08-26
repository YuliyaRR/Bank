package org.example.dao.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder(setterPrefix = "set")
@ToString
public class AccountEntity {
    private UUID num;
    private String currency;
    private BankEntity bank;
    private LocalDate dateOpen;
    private LocalDateTime dateLastTransaction;
    private double balance;
    private ClientEntity owner;


}
