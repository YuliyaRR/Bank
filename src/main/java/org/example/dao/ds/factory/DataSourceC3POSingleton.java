package org.example.dao.ds.factory;

import org.example.dao.ds.api.IDataSourceWrapper;
import org.example.dao.ds.impl.DataSourceC3PO;

import java.beans.PropertyVetoException;
import java.util.Properties;

public class DataSourceC3POSingleton {
    private static Properties properties;
    private volatile static IDataSourceWrapper instance;

    private DataSourceC3POSingleton(){
    }

    public static void setProperties(Properties properties){
        synchronized (DataSourceC3POSingleton.class) {
            if (instance != null) {
                throw new IllegalStateException("You can't change settings if the database connection is already created");
            }
            DataSourceC3POSingleton.properties = properties;
        }
    }

    public static IDataSourceWrapper getInstance() throws PropertyVetoException {
        if (instance == null){
            synchronized (DataSourceC3POSingleton.class){
                if(instance == null) {
                    instance = new DataSourceC3PO(properties);
                }
            }
        }
        return instance;
    }




}
