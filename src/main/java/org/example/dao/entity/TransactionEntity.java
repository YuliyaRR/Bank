package org.example.dao.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder(setterPrefix = "set")
public class TransactionEntity {
    private UUID id;
    private String currency;
    private LocalDateTime date;
    private UUID accountFrom;
    private UUID accountTo;
    private double sum;
    private String type;
}
