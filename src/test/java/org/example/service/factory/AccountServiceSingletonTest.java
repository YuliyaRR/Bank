package org.example.service.factory;

import org.example.dao.repositories.factory.AccountRepositorySingleton;
import org.example.dao.repositories.impl.AccountRepository;
import org.example.service.api.IAccountService;
import org.example.service.impl.BankService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceSingletonTest {
    private AccountRepository accountRepository;
    private BankService bankService;

    @Test
    public void isSingleton() {
        try (MockedStatic<AccountRepositorySingleton> singleAccountRepo = mockStatic(AccountRepositorySingleton.class);
             MockedStatic<BankServiceSingleton> singleBankService = mockStatic(BankServiceSingleton.class)) {

            singleAccountRepo.when(AccountRepositorySingleton::getInstance).thenReturn(accountRepository);
            singleBankService.when(BankServiceSingleton::getInstance).thenReturn(bankService);

            IAccountService instance1 = AccountServiceSingleton.getInstance();
            IAccountService instance2 = AccountServiceSingleton.getInstance();

            assertNotNull(instance1);
            assertNotNull(instance2);
            assertSame(instance1, instance2);
        }
    }
}