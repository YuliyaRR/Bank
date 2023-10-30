package org.example.listener.impl.accountStatement;

import org.example.core.dto.docs.AccountStatement;
import org.example.core.events.AccountStatementEvent;
import org.example.service.api.IDocCreationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AccountStatementListenerTest {
    private AccountStatementListener accountStatementListener;
    @Mock
    private IDocCreationService docCreationService;
    private AccountStatementEvent accountStatementEvent;

    @BeforeEach
    public void setUp() {
        this.accountStatementListener = new AccountStatementListener(docCreationService);
        this.accountStatementEvent = new AccountStatementEvent(new AccountStatement());
    }

    @Test
    public void handleEvent() {
        accountStatementListener.handleEvent(accountStatementEvent);
        verify(docCreationService, times(1)).createAccountStatement(any(AccountStatement.class));
    }
}