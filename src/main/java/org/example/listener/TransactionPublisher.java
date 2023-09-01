package org.example.listener;

import org.example.core.events.TransactionEvent;

import java.util.List;


public class TransactionPublisher implements IPublisher<TransactionEvent>{
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