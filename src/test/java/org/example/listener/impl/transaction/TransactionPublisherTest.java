package org.example.listener.impl.transaction;

import org.example.core.dto.Transaction;
import org.example.core.events.TransactionEvent;
import org.example.listener.api.IListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionPublisherTest {
    private TransactionPublisher publisher;
    @Mock
    private IListener<TransactionEvent> listener;
    private TransactionEvent transactionEvent;

    @BeforeEach
    public void setUp() {
        this.publisher = new TransactionPublisher(listener);
        this.transactionEvent = new TransactionEvent(new Transaction());
    }

    @Test
    public void notifyTransactionEvent() {
        publisher.notify(transactionEvent);
        verify(listener, times(1)).handleEvent(any(TransactionEvent.class));
    }

    @Test
    public void notifyTransactionEvents() {
        publisher.notify(List.of(transactionEvent));
        verify(listener, times(1)).handleEvents(anyList());
    }
}