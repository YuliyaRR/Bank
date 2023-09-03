package org.example.service.api;

import org.example.core.dto.AccountStatement;
import org.example.core.dto.Check;

public interface IDocCreationService {
    void createCheck(Check check);
    void createAccountStatement(AccountStatement accountStatement);
}
