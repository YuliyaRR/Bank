package org.example.dao.repositories.api;

import org.example.core.dto.Period;
import org.example.core.dto.SumTransactionsInfo;
import org.example.dao.entity.TransactionEntity;

import java.util.List;
import java.util.UUID;

public interface ITransactionRepository {
    void saveTransaction(TransactionEntity entity);
    void saveMonthlyInterestTransactions(List<TransactionEntity> entities);
    List<TransactionEntity> allAccountTransactions(UUID account, Period period);
    SumTransactionsInfo getSumInfoAboutTransactions(UUID account, Period period);
}
