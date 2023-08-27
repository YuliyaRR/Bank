package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.dto.Transaction;
import org.example.dao.api.ITransactionRepository;
import org.example.dao.entity.TransactionEntity;
import org.example.service.api.ITransactionService;

@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final ITransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        TransactionEntity entity = TransactionEntity.builder()
                .setId(transaction.getId())
                .setCurrency(transaction.getCurrency().name())
                .setDate(transaction.getDate())
                .setAccountFrom(transaction.getAccountFrom())
                .setAccountTo(transaction.getAccountTo())
                .setSum(transaction.getSum())
                .setType(transaction.getType().name())
                .build();

        transactionRepository.save(entity);
    }
}
