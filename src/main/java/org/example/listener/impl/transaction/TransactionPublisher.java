package org.example.listener.impl.transaction;

import org.example.core.events.TransactionEvent;
import org.example.listener.api.IListener;
import org.example.listener.api.IPublisher;

import java.util.List;


public class TransactionPublisher implements IPublisher<TransactionEvent> {
    private final IListener<TransactionEvent> listener;

    public TransactionPublisher() {
       listener = new TransactionListener();
    }

    @Override
    public void notify(TransactionEvent event) {
        listener.handleEvent(event);
    }

    @Override
    public void notify(List<TransactionEvent> events) {
        listener.handleEvents(events);
    }
}