package org.example.service.api;

import org.example.core.dto.Transaction;

import java.util.List;

public interface ITransactionService {
    void saveTransaction(Transaction transaction);
    void saveMonthlyInterestTransactions(List<Transaction> transactions);
}
