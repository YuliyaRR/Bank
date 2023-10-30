package org.example.service.factory;

import org.example.dao.repositories.factory.BankRepositorySingleton;
import org.example.dao.repositories.impl.BankRepository;
import org.example.service.api.IBankService;
import org.junit.jupiter.api.Test;

import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BankServiceSingletonTest {
    private BankRepository bankRepository;
    @Test
    public void isSingleton() {
        try(MockedStatic<BankRepositorySingleton> singleBankRepo = mockStatic(BankRepositorySingleton.class)){

            singleBankRepo.when(BankRepositorySingleton::getInstance).thenReturn(bankRepository);

            IBankService instance1 = BankServiceSingleton.getInstance();
            IBankService instance2 = BankServiceSingleton.getInstance();

            assertNotNull(instance1);
            assertNotNull(instance2);
            assertSame(instance1, instance2);
        }
    }


}