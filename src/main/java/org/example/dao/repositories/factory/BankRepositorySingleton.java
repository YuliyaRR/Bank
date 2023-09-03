package org.example.dao.repositories.factory;

import org.example.dao.ds.factory.DataSourceC3POSingleton;
import org.example.dao.repositories.api.IBankRepository;
import org.example.dao.repositories.impl.BankRepository;

import java.beans.PropertyVetoException;
import java.util.Properties;

public class BankRepositorySingleton {
    private static Properties properties;
    private volatile static IBankRepository instance;

    private BankRepositorySingleton() {
    }

    public static void setProperties(Properties properties) {
        synchronized (BankRepositorySingleton.class) {
            if (instance != null) {
                throw new IllegalStateException("You can't change settings if the database connection is already created");
            }
            BankRepositorySingleton.properties = properties;
        }
    }

    public static IBankRepository getInstance() throws PropertyVetoException {
        if (instance == null){
            synchronized (BankRepositorySingleton.class){
                if(instance == null) {
                    instance = new BankRepository(DataSourceC3POSingleton.getInstance(), properties);
                }
            }
        }
        return instance;
    }
}
