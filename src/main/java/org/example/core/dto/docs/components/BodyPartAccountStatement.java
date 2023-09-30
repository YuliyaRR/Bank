package org.example.core.dto.docs.components;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.example.core.dto.Currency;
import org.example.core.dto.Transaction;

import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class BodyPartAccountStatement {
    private List<Transaction> transactions;
    private Currency currency;
}
