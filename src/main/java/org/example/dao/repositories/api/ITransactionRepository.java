package org.example.dao.repositories.api;

import org.example.dao.entity.TransactionEntity;

import java.util.List;

public interface ITransactionRepository {
    void saveTransaction(TransactionEntity entity);
    void saveMonthlyInterestTransactions(List<TransactionEntity> entities);
}
