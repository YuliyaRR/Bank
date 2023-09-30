package org.example.service.api;

import org.example.core.dto.docs.AccountStatement;
import org.example.core.dto.docs.Check;
import org.example.core.dto.docs.MoneyStatement;

public interface IDocCreationService {
    void createCheck(Check check);
    void createAccountStatement(AccountStatement accountStatement);
    void createMoneyStatement(MoneyStatement moneyStatement);
}
