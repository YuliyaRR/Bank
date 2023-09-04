package org.example.service.factory;

import org.example.dao.repositories.factory.ClientRepositorySingleton;
import org.example.service.api.IClientService;
import org.example.service.impl.ClientService;

import java.beans.PropertyVetoException;

public class ClientServiceSingleton {
    private volatile static IClientService instance;

    private ClientServiceSingleton() {
    }

    public static IClientService getInstance() {
        if(instance == null) {
            synchronized (ClientServiceSingleton.class) {
                if(instance == null) {
                    try {
                        instance =  new ClientService(ClientRepositorySingleton.getInstance());
                    } catch (PropertyVetoException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
        return instance;
    }
}
