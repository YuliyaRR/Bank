package org.example.listener.impl.moneyStatement;

import org.example.core.events.MoneyStatementEvent;
import org.example.listener.api.IListener;
import org.example.service.api.IDocCreationService;
import org.example.service.factory.DocCreationServiceSingleton;

import java.beans.PropertyVetoException;
import java.util.List;

public class MoneyStatementListener implements IListener<MoneyStatementEvent> {
    private final IDocCreationService docCreationService;

    public MoneyStatementListener() {
        try {
            this.docCreationService = DocCreationServiceSingleton.getInstance();
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleEvent(MoneyStatementEvent event) {
        docCreationService.createMoneyStatement(event.getMoneyStatement());
    }

    @Override
    public void handleEvents(List<MoneyStatementEvent> events) {}
}
