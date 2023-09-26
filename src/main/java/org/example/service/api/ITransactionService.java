package org.example.service.api;

import org.example.core.dto.Period;
import org.example.core.dto.SumTransactionsInfo;
import org.example.core.dto.Transaction;

import java.util.List;
import java.util.UUID;

public interface ITransactionService {
    void saveTransaction(Transaction transaction);
    void saveMonthlyInterestTransactions(List<Transaction> transactions);
    List<Transaction> allAccountTransactions(UUID account, Period period);
    SumTransactionsInfo getSumInfoAboutTransactions(UUID account, Period period);
}
