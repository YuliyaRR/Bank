package org.example.service.api;

import org.example.core.dto.Currency;
import org.example.core.dto.Transaction;

import java.util.UUID;

public interface IAccountService {
    void addMoney(UUID accountTo, double sum, Currency currency);
    void withdrawalMoney(UUID accountFrom, double sum, Currency currency);
    void transferMoney(Transaction transaction);


}
