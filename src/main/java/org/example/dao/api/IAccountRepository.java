package org.example.dao.api;

import org.example.dao.entity.AccountEntity;

import java.util.UUID;

public interface IAccountRepository {
    boolean doesExist(UUID account);
    boolean doesEnoughMoney(UUID account, double sum);
    AccountEntity updateBalance(UUID account, double sum);
}
