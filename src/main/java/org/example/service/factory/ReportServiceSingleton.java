package org.example.service.factory;

import org.example.listener.impl.accountStatement.AccountStatementPublisher;
import org.example.service.api.IReportService;
import org.example.service.impl.ReportService;

import java.util.Properties;


public class ReportServiceSingleton {
    private volatile static IReportService instance;
    private static Properties properties;


    private ReportServiceSingleton() {
    }

    public static void setProperties(Properties properties) {
        synchronized (ReportServiceSingleton.class) {
            if (instance != null) {
                throw new IllegalStateException("You can't change settings if the database connection is already created");
            }
            ReportServiceSingleton.properties = properties;
        }
    }


    public static IReportService getInstance() {
        if(instance == null) {
            synchronized (ReportServiceSingleton.class) {
                if(instance == null) {
                    instance =  new ReportService(
                            AccountServiceSingleton.getInstance(),
                            TransactionServiceSingleton.getInstance(),
                            new AccountStatementPublisher(),
                            properties
                            );
                }
            }
        }
        return instance;
    }
}
