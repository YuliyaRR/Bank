package org.example.dao.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.core.dto.TransactionType;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class TransactionTypeEntity {
    private TransactionType type;
}
