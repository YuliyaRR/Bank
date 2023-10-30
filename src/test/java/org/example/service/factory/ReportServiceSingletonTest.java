package org.example.service.factory;

import org.example.service.api.IReportService;
import org.example.service.impl.AccountService;
import org.example.service.impl.ReportService;
import org.example.service.impl.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
@ExtendWith(MockitoExtension.class)
public class ReportServiceSingletonTest {
    @Mock
    private ReportService reportService;
    private AccountService accountService;
    private TransactionService transactionService;
    private static final String EXCEPTION_MESSAGE = "You can't change settings if the database connection is already created";

    @Test
    public void setPropertiesWhenPropertiesFieldIsNullThenSuccessfully() throws NoSuchFieldException, IllegalAccessException {
        Field instance = ReportServiceSingleton.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(ReportServiceSingleton.class, null);

        Field properties = ReportServiceSingleton.class.getDeclaredField("properties");
        properties.setAccessible(true);
        properties.set(ReportServiceSingleton.class, null);

        Properties props = new Properties();
        ReportServiceSingleton.setProperties(props);

        assertNotNull(properties);
    }

    @Test
    public void setPropertiesWhenPropertiesFieldNotNullThenThrowException() throws NoSuchFieldException, IllegalAccessException {
        Field instance = ReportServiceSingleton.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(ReportServiceSingleton.class, reportService);

        Properties props = new Properties();
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> ReportServiceSingleton.setProperties(props));
        assertEquals(EXCEPTION_MESSAGE, exception.getMessage());
    }

    @Test
    public void isSingleton() {
        try (MockedStatic<AccountServiceSingleton> singleAccountService = mockStatic(AccountServiceSingleton.class);
             MockedStatic<TransactionServiceSingleton> singleTransactionService = mockStatic(TransactionServiceSingleton.class)) {

            singleAccountService.when(AccountServiceSingleton::getInstance).thenReturn(accountService);
            singleTransactionService.when(TransactionServiceSingleton::getInstance).thenReturn(transactionService);

            IReportService instance1 = ReportServiceSingleton.getInstance();
            IReportService instance2 = ReportServiceSingleton.getInstance();


            assertNotNull(instance1);
            assertNotNull(instance2);
            assertSame(instance1, instance2);
        }
    }
}