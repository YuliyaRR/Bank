package org.example.service.api;

import org.example.core.dto.Transaction;

import java.util.UUID;

public interface IAccountService {
    void addMoney(UUID accountTo, double sum);
    void withdrawalMoney(UUID accountFrom, double sum);
    void transferMoney(Transaction transaction);


}
