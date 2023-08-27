package org.example.core.dto;

import lombok.*;

import java.time.LocalDateTime;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder(setterPrefix = "set")
public class Transaction {
    private UUID id;
    private Currency currency;
    private LocalDateTime date;
    private UUID accountFrom;
    private UUID accountTo;
    private double sum;
    private TransactionType type;
}
