package org.example.dao.repositories.api;

import org.example.core.dto.Transaction;
import org.example.dao.entity.AccountEntity;

import java.util.UUID;

public interface IAccountRepository {
    AccountEntity checkAccountExistence(UUID account);
    Transaction updateBalanceCashOperation(Transaction transaction);
    Transaction updateBalanceCashlessPayments(Transaction transaction);
    void calculateMonthlyInterest();
    AccountEntity getAccountInfo(UUID uuid);
}
