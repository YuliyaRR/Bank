package org.example.listener.impl.transaction;

import org.example.core.dto.Transaction;
import org.example.core.events.TransactionEvent;
import org.example.service.api.ITransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class TransactionListenerTest {
    private TransactionListener transactionListener;
    @Mock
    private ITransactionService transactionService;
    private TransactionEvent transactionEvent;

    @BeforeEach
    public void setUp() {
        this.transactionListener = new TransactionListener(transactionService);
        this.transactionEvent = new TransactionEvent(new Transaction());
    }

    @Test
    public void handleEvent() {
        transactionListener.handleEvent(transactionEvent);
        verify(transactionService, times(1)).saveTransaction(transactionEvent.getTransaction());
    }

    @Test
    public void handleEvents() {
        List<TransactionEvent> list = new ArrayList<>(List.of(transactionEvent));
        transactionListener.handleEvents(list);
        verify(transactionService, times(1)).saveMonthlyInterestTransactions(anyList());
    }
}