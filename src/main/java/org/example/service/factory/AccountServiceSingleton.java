package org.example.service.factory;

import org.example.dao.repositories.factory.AccountRepositorySingleton;
import org.example.service.api.IAccountService;
import org.example.service.impl.AccountService;

import java.beans.PropertyVetoException;

public class AccountServiceSingleton {
    private volatile static IAccountService instance;

    private AccountServiceSingleton() {
    }

    public static IAccountService getInstance() {
        if(instance == null) {
            synchronized (AccountServiceSingleton.class) {
                if(instance == null) {
                    try {
                        instance =  new AccountService(
                                AccountRepositorySingleton.getInstance(),
                                TransactionServiceSingleton.getInstance()
                        );
                    }catch (PropertyVetoException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
        return instance;
    }
}
