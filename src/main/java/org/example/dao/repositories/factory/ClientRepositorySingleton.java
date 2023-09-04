package org.example.dao.repositories.factory;

import org.example.dao.ds.factory.DataSourceC3POSingleton;
import org.example.dao.repositories.api.IClientRepository;
import org.example.dao.repositories.impl.ClientRepository;

import java.beans.PropertyVetoException;
import java.util.Properties;

public class ClientRepositorySingleton {
    private static Properties properties;
    private volatile static IClientRepository instance;

    private ClientRepositorySingleton() {
    }

    public static void setProperties(Properties properties) {
        synchronized (ClientRepositorySingleton.class) {
            if (instance != null) {
                throw new IllegalStateException("You can't change settings if the database connection is already created");
            }
            ClientRepositorySingleton.properties = properties;
        }
    }

    public static IClientRepository getInstance() throws PropertyVetoException {
        if (instance == null){
            synchronized (ClientRepositorySingleton.class){
                if(instance == null) {
                    instance = new ClientRepository(DataSourceC3POSingleton.getInstance(), properties);
                }
            }
        }
        return instance;
    }
}
