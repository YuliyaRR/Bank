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
public class TransactionEntity {
    private UUID id;
    private CurrencyEntity currency;
    private LocalDate date;
    private AccountEntity accountFrom;
    private AccountEntity accountTo;
    private double sum;
    private TransactionTypeEntity type;
}
