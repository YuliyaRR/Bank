package org.example.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.ds.api.IDataSourceWrapper;
import org.example.dao.ds.factory.DataSourceC3POSingleton;
import org.example.dao.repositories.factory.AccountRepositorySingleton;
import org.example.dao.repositories.factory.BankRepositorySingleton;
import org.example.dao.repositories.factory.TransactionRepositorySingleton;
import org.example.service.api.IAccountService;
import org.example.service.factory.AccountServiceSingleton;
import org.example.service.factory.DocCreationServiceSingleton;
import org.example.service.factory.ObjectMapperHelperSingleton;
import org.example.service.factory.ReportServiceSingleton;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class PropertiesLoaderListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ObjectMapper mapper = ObjectMapperHelperSingleton.getObjectMapper();
        File confDir = new File(System.getenv("catalina_base") + "/conf");

        String documentsCheck = servletContextEvent.getServletContext().getRealPath("/check");
        File documentsFolderCheck = new File(documentsCheck);
        if (!documentsFolderCheck.exists()) {
            documentsFolderCheck.mkdirs();
        }

        System.setProperty("PATH_FOR_SAVING_CHECK", documentsCheck);

        String documentsAccountStatement = servletContextEvent.getServletContext().getRealPath("/statement");
        File documentsFolderAccountStatement = new File(documentsAccountStatement);
        if (!documentsFolderAccountStatement.exists()) {
            documentsFolderAccountStatement.mkdirs();
        }

        System.setProperty("PATH_FOR_SAVING_ACCOUNT_STATEMENT", documentsAccountStatement);

        try {
            Properties properties = mapper.readValue(new File(confDir + "/application.yml"), Properties.class);

            DataSourceC3POSingleton.setProperties(properties);
            AccountRepositorySingleton.setProperties(properties);
            TransactionRepositorySingleton.setProperties(properties);
            BankRepositorySingleton.setProperties(properties);
            ReportServiceSingleton.setProperties(properties);
            DocCreationServiceSingleton.setProperties(properties);

            IAccountService accountService = AccountServiceSingleton.getInstance();

            ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

            scheduledExecutor.scheduleAtFixedRate(() -> accountService.checkTheNeedToCalculateInterest(),
                    1000, 30000, TimeUnit.MILLISECONDS);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            IDataSourceWrapper dataSource = DataSourceC3POSingleton.getInstance();
            dataSource.close();
            DriverManager.getDrivers().asIterator().forEachRemaining(driver -> {
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
