package org.example.dao.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class AccountEntity {
    private UUID num;
    private CurrencyEntity currency;
    private BankEntity bank;
    private LocalDate dateOpen;
    private double balance;
    private ClientEntity owner;


}
