package org.example.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.core.dto.TransactionType;

@Getter
@Setter
@EqualsAndHashCode
public class TransactionTypeEntity {
    private String name;

    public TransactionTypeEntity(TransactionType type) {
        this.name = type.name();
    }
}
