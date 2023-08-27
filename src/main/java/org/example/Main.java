package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.example.dao.api.IAccountRepository;
import org.example.dao.api.ITransactionRepository;
import org.example.dao.ds.api.IDataSourceWrapper;
import org.example.dao.ds.factory.DataSourceC3POSingleton;
import org.example.dao.factory.AccountRepositorySingleton;
import org.example.dao.factory.TransactionRepositorySingleton;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException, PropertyVetoException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        Properties properties = mapper.readValue(new File("src/main/resources/application.yml"), Properties.class);

        DataSourceC3POSingleton.setProperties(properties);
        IDataSourceWrapper dataSource = DataSourceC3POSingleton.getInstance();

        AccountRepositorySingleton.setProperties(properties);
        IAccountRepository repository = AccountRepositorySingleton.getInstance();

        TransactionRepositorySingleton.setProperties(properties);
        ITransactionRepository transactionRepository = TransactionRepositorySingleton.getInstance();

    }
}