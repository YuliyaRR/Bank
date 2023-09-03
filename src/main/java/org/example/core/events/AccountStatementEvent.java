package org.example.core.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.core.dto.AccountStatement;

@Getter
@RequiredArgsConstructor
public class AccountStatementEvent {
    private final AccountStatement accountStatement;
}
