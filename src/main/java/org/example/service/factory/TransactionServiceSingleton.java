package org.example.service.factory;

import org.example.dao.repositories.factory.TransactionRepositorySingleton;
import org.example.service.api.ITransactionService;
import org.example.service.impl.TransactionService;

import java.beans.PropertyVetoException;

public class TransactionServiceSingleton {
    private volatile static ITransactionService instance;

    private TransactionServiceSingleton() {
    }

    public static ITransactionService getInstance() {
        if(instance == null) {
            synchronized (TransactionServiceSingleton.class) {
                if(instance == null) {
                    try {
                        instance =  new TransactionService(TransactionRepositorySingleton.getInstance());
                    } catch (PropertyVetoException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
        return instance;
    }
}
