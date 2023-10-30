package org.example.listener.impl.moneyStatement;

import org.example.core.dto.docs.MoneyStatement;
import org.example.core.events.MoneyStatementEvent;
import org.example.service.api.IDocCreationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MoneyStatementListenerTest {
    private MoneyStatementListener moneyStatementListener;
    @Mock
    private IDocCreationService docCreationService;
    private MoneyStatementEvent moneyStatementEvent;
    @BeforeEach
    public void setUp() {
        this.moneyStatementListener = new MoneyStatementListener(docCreationService);
        this.moneyStatementEvent = new MoneyStatementEvent(new MoneyStatement());
    }

    @Test
    public void handleEvent() {
        moneyStatementListener.handleEvent(moneyStatementEvent);
        verify(docCreationService, times(1)).createMoneyStatement(any(MoneyStatement.class));
    }
}