package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.example.dao.ds.factory.DataSourceC3POSingleton;
import org.example.dao.repositories.factory.AccountRepositorySingleton;
import org.example.dao.repositories.factory.TransactionRepositorySingleton;
import org.example.service.api.IAccountService;
import org.example.service.api.ITransactionService;
import org.example.service.factory.AccountServiceSingleton;
import org.example.service.factory.TransactionServiceSingleton;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws IOException{
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Properties properties = mapper.readValue(new File("src/main/resources/application.yml"), Properties.class);

        DataSourceC3POSingleton.setProperties(properties);
        AccountRepositorySingleton.setProperties(properties);
        TransactionRepositorySingleton.setProperties(properties);

        IAccountService accountService = AccountServiceSingleton.getInstance();
        ITransactionService transactionService = TransactionServiceSingleton.getInstance();

        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutor.scheduleAtFixedRate(() -> accountService.checkTheNeedToCalculateInterest(),
                1000, 30000, TimeUnit.MILLISECONDS);
    }
}