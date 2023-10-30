package org.example.listener.impl.transaction;

import lombok.RequiredArgsConstructor;
import org.example.core.events.TransactionEvent;
import org.example.listener.api.IListener;
import org.example.service.api.ITransactionService;
import org.example.service.factory.TransactionServiceSingleton;

import java.util.List;

@RequiredArgsConstructor
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
                .map(TransactionEvent::getTransaction)
                .toList()
        );


    }
}
