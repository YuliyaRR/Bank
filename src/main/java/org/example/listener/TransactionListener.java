package org.example.listener;

import org.example.core.events.TransactionEvent;
import org.example.service.api.ITransactionService;
import org.example.service.factory.TransactionServiceSingleton;

import java.util.List;


public class TransactionListener implements IListener<TransactionEvent> {
    private final ITransactionService transactionService;

    public TransactionListener() {
        this.transactionService = TransactionServiceSingleton.getInstance();
    }

    @Override
    public void handleEvent(TransactionEvent event) {
        transactionService.saveTransaction(event.getTransaction());
    }

    @Override
    public void handleEvents(List<TransactionEvent> events) {
        transactionService.saveMonthlyInterestTransactions(
                events.stream()
                .map(event -> event.getTransaction())
                .toList()
        );


    }
}
