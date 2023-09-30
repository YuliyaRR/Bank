package org.example.core.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.core.dto.docs.MoneyStatement;

@Getter
@RequiredArgsConstructor
public class MoneyStatementEvent {
    private final MoneyStatement moneyStatement;
}
