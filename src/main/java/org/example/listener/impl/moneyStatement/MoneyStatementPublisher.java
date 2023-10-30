package org.example.listener.impl.moneyStatement;

import lombok.RequiredArgsConstructor;
import org.example.core.events.MoneyStatementEvent;
import org.example.listener.api.IListener;
import org.example.listener.api.IPublisher;

import java.util.List;

@RequiredArgsConstructor
public class MoneyStatementPublisher implements IPublisher<MoneyStatementEvent> {
    private final IListener<MoneyStatementEvent> listener;

    public MoneyStatementPublisher() {
       listener = new MoneyStatementListener();
    }

    @Override
    public void notify(MoneyStatementEvent event) {
        listener.handleEvent(event);
    }

    @Override
    public void notify(List<MoneyStatementEvent> events) {
    }
}