package org.example.service.factory;

import org.example.service.api.IDocCreationService;
import org.example.service.impl.DocCreationService;

import java.beans.PropertyVetoException;
import java.util.Properties;


public class DocCreationServiceSingleton {
    private volatile static IDocCreationService instance;
    private static Properties properties;


    private DocCreationServiceSingleton() {
    }

    public static void setProperties(Properties properties) {
        synchronized (DocCreationServiceSingleton.class) {
            if (instance != null) {
                throw new IllegalStateException("You can't change settings if the database connection is already created");
            }
            DocCreationServiceSingleton.properties = properties;
        }
    }


    public static IDocCreationService getInstance() throws PropertyVetoException {
        if(instance == null) {
            synchronized (DocCreationServiceSingleton.class) {
                if(instance == null) {
                    instance =  new DocCreationService(properties);
                }
            }
        }
        return instance;
    }
}
