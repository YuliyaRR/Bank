package org.example.dao.factory;

import org.example.dao.api.IAccountRepository;
import org.example.dao.ds.factory.DataSourceC3POSingleton;
import org.example.dao.impl.AccountRepository;

import java.beans.PropertyVetoException;
import java.util.Properties;

public class AccountRepositorySingleton {
    private static Properties properties;
    private volatile static IAccountRepository instance;

    private AccountRepositorySingleton() {
    }

    public static void setProperties(Properties properties) {
        synchronized (AccountRepositorySingleton.class) {
            if (instance != null) {
                throw new IllegalStateException("You can't change settings if the database connection is already created");
            }
            AccountRepositorySingleton.properties = properties;
        }
    }

    public static IAccountRepository getInstance() throws PropertyVetoException {
        if (instance == null){
            synchronized (AccountRepositorySingleton.class){
                if(instance == null) {
                    instance = new AccountRepository(DataSourceC3POSingleton.getInstance(),properties);
                }
            }
        }
        return instance;
    }
}
