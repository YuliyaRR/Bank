package org.example.listener;

import org.example.core.events.TransactionEvent;


public class TransactionPublisher implements IPublisher<TransactionEvent>{
    private final IListener<TransactionEvent> listener;

    public TransactionPublisher() {
       listener = new TransactionListener();
    }

    @Override
    public void notify(TransactionEvent event) {
        listener.handleEvent(event);
    }

}