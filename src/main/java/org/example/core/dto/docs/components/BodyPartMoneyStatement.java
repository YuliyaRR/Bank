package org.example.core.dto.docs.components;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.example.core.dto.Currency;
import org.example.core.dto.SumTransactionsInfo;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class BodyPartMoneyStatement {
    private SumTransactionsInfo sumTransactionsInfo;
    private Currency currency;
}
