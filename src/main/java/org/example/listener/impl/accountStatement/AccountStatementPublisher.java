package org.example.listener.impl.accountStatement;

import lombok.RequiredArgsConstructor;
import org.example.core.events.AccountStatementEvent;
import org.example.listener.api.IListener;
import org.example.listener.api.IPublisher;

import java.util.List;

@RequiredArgsConstructor
public class AccountStatementPublisher implements IPublisher<AccountStatementEvent> {
    private final IListener<AccountStatementEvent> listener;

    public AccountStatementPublisher() {
       listener = new AccountStatementListener();
    }

    @Override
    public void notify(AccountStatementEvent event) {
        listener.handleEvent(event);
    }

    @Override
    public void notify(List<AccountStatementEvent> events) {
        listener.handleEvents(events);
    }
}