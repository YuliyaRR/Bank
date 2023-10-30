package org.example.service.factory;

import org.example.dao.repositories.factory.TransactionRepositorySingleton;
import org.example.dao.repositories.impl.TransactionRepository;
import org.example.service.api.ITransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

public class TransactionServiceSingletonTest {
    private TransactionRepository transactionRepository;

    @Test
    public void isSingleton() {
        try (MockedStatic<TransactionRepositorySingleton> singleTransactionRepo = mockStatic(TransactionRepositorySingleton.class)){
            singleTransactionRepo.when(TransactionRepositorySingleton::getInstance).thenReturn(transactionRepository);

            ITransactionService instance1 = TransactionServiceSingleton.getInstance();
            ITransactionService instance2 = TransactionServiceSingleton.getInstance();

            assertNotNull(instance1);
            assertNotNull(instance2);
            assertSame(instance1, instance2);
        }
    }
}