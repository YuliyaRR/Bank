package org.example.service.api;

import org.example.core.dto.Account;
import org.example.core.dto.Check;
import org.example.core.dto.Transaction;

import java.util.UUID;

public interface IAccountService {
    Check addMoney(Transaction transaction);
    Check withdrawalMoney(Transaction transaction);
    Check transferMoney(Transaction transaction);
    void checkTheNeedToCalculateInterest();
    void calculateInterest();
    Account getAccountInfo(UUID account);


}
