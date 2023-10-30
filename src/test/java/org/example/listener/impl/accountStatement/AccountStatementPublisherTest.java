package org.example.listener.impl.accountStatement;

import org.example.core.dto.docs.AccountStatement;
import org.example.core.events.AccountStatementEvent;
import org.example.listener.api.IListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountStatementPublisherTest {
    private AccountStatementPublisher publisher;
    @Mock
    private IListener<AccountStatementEvent> listener;
    private AccountStatementEvent event;

    @BeforeEach
    public void setUp() {
        this.publisher = new AccountStatementPublisher(listener);
        this.event = new AccountStatementEvent(new AccountStatement());
    }

    @Test
    public void notifyAccountStatementEvent() {
        publisher.notify(event);
        verify(listener, times(1)).handleEvent(any(AccountStatementEvent.class));
    }
}