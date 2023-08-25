package org.example.dao.ds.impl;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.example.dao.ds.api.IDataSourceWrapper;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceC3PO implements IDataSourceWrapper {
    private final String DRIVER_CLASS_PROPERTY_NAME = "driverDataSource";
    private final String URL_PROPERTY_NAME = "urlDB";
    private final String USER_PROPERTY_NAME = "usernameDB";
    private final String PASSWORD_PROPERTY_NAME = "passwordDB";

    private final ComboPooledDataSource dataSource;

    public DataSourceC3PO(Properties properties) throws PropertyVetoException {
        this.dataSource = new ComboPooledDataSource();
        this.dataSource.setDriverClass(properties.getProperty(DRIVER_CLASS_PROPERTY_NAME));
        this.dataSource.setJdbcUrl(properties.getProperty(URL_PROPERTY_NAME));
        this.dataSource.setUser(properties.getProperty(USER_PROPERTY_NAME));
        this.dataSource.setPassword(properties.getProperty(PASSWORD_PROPERTY_NAME));

 }

    @Override
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    @Override
    public void close() throws Exception {
        this.dataSource.close();
    }


}