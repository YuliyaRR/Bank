package org.example.listener.impl.accountStatement;

import lombok.RequiredArgsConstructor;
import org.example.core.events.AccountStatementEvent;
import org.example.listener.api.IListener;
import org.example.service.api.IDocCreationService;
import org.example.service.factory.DocCreationServiceSingleton;

import java.beans.PropertyVetoException;
import java.util.List;
@RequiredArgsConstructor
public class AccountStatementListener implements IListener<AccountStatementEvent> {
    private final IDocCreationService docCreationService;

    public AccountStatementListener() {
        try {
            this.docCreationService = DocCreationServiceSingleton.getInstance();
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleEvent(AccountStatementEvent event) {
        docCreationService.createAccountStatement(event.getAccountStatement());
    }

    @Override
    public void handleEvents(List<AccountStatementEvent> events) {}
}
