package org.example.dao.repositories.factory;

import org.example.dao.repositories.api.ITransactionRepository;
import org.example.dao.ds.factory.DataSourceC3POSingleton;
import org.example.dao.repositories.impl.TransactionRepository;

import java.beans.PropertyVetoException;
import java.util.Properties;

public class TransactionRepositorySingleton {
    private static Properties properties;
    private volatile static ITransactionRepository instance;

    private TransactionRepositorySingleton() {
    }

    public static void setProperties(Properties properties) {
        synchronized (TransactionRepositorySingleton.class) {
            if (instance != null) {
                throw new IllegalStateException("You can't change settings if the database connection is already created");
            }
            TransactionRepositorySingleton.properties = properties;
        }
    }

    public static ITransactionRepository getInstance() throws PropertyVetoException {
        if (instance == null){
            synchronized (TransactionRepositorySingleton.class){
                if(instance == null) {
                    instance = new TransactionRepository(DataSourceC3POSingleton.getInstance(), properties);
                }
            }
        }
        return instance;
    }
}
