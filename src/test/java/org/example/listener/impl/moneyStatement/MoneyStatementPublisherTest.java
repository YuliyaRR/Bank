package org.example.listener.impl.moneyStatement;

import org.example.core.dto.docs.MoneyStatement;
import org.example.core.events.MoneyStatementEvent;
import org.example.listener.api.IListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class MoneyStatementPublisherTest {
    private MoneyStatementPublisher moneyStatementPublisher;
    @Mock
    private IListener<MoneyStatementEvent> listener;
    private MoneyStatementEvent event;

    @BeforeEach
    public void setUp() {
        this.moneyStatementPublisher = new MoneyStatementPublisher(listener);
        this.event = new MoneyStatementEvent(new MoneyStatement());
    }

    @Test
    public void notifyMoneyStatementEvent() {
        moneyStatementPublisher.notify(event);
        verify(listener, times(1)).handleEvent(any(MoneyStatementEvent.class));
    }
}