package org.example.core.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.core.dto.docs.AccountStatement;

@Getter
@RequiredArgsConstructor
public class AccountStatementEvent {
    private final AccountStatement accountStatement;
}
