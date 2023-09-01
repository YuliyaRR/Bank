package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.dto.Transaction;
import org.example.dao.repositories.api.ITransactionRepository;
import org.example.dao.entity.TransactionEntity;
import org.example.service.api.ITransactionService;

import java.util.List;

@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    private final ITransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.saveTransaction(convertToEntity(transaction));
    }

    @Override
    public void saveMonthlyInterestTransactions(List<Transaction> transactions) {
        transactionRepository.saveMonthlyInterestTransactions(
                transactions.stream()
                .map(transaction -> convertToEntity(transaction))
                .toList()
        );

    }

    private TransactionEntity convertToEntity(Transaction transaction) {
        return TransactionEntity.builder()
                .setId(transaction.getId())
                .setCurrency(transaction.getCurrency().name())
                .setDate(transaction.getDate())
                .setAccountFrom(transaction.getAccountFrom())
                .setAccountTo(transaction.getAccountTo())
                .setSum(transaction.getSum())
                .setType(transaction.getType().name())
                .build();
    }
}
