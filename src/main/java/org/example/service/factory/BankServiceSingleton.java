package org.example.service.factory;


import org.example.dao.repositories.factory.BankRepositorySingleton;
import org.example.service.api.IBankService;
import org.example.service.impl.BankService;

import java.beans.PropertyVetoException;


public class BankServiceSingleton {
    private volatile static IBankService instance;

    private BankServiceSingleton() {
    }

    public static IBankService getInstance() {
        if(instance == null) {
            synchronized (BankServiceSingleton.class) {
                if(instance == null) {
                    try {
                        instance = new BankService(BankRepositorySingleton.getInstance());
                    }catch (PropertyVetoException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
        return instance;
    }
}
