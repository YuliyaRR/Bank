package org.example.dao.repositories.api;

import org.example.core.dto.Transaction;
import org.example.dao.entity.AccountEntity;

import java.util.UUID;

public interface IAccountRepository {
    AccountEntity checkAccount(UUID account);
    void updateBalanceCashOperation(Transaction transaction);
    void updateBalanceCashlessPayments(Transaction transaction);
}
