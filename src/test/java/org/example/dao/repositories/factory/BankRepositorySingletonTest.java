package org.example.dao.repositories.factory;

import org.example.dao.ds.api.IDataSourceWrapper;
import org.example.dao.ds.factory.DataSourceC3POSingleton;
import org.example.dao.repositories.api.IBankRepository;
import org.example.dao.repositories.impl.BankRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.beans.PropertyVetoException;
import java.lang.reflect.Field;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
@ExtendWith(MockitoExtension.class)
public class BankRepositorySingletonTest {
    @Mock
    private BankRepository bankRepository;
    private IDataSourceWrapper dataSourceWrapper;
    private static final String EXCEPTION_MESSAGE = "You can't change settings if the database connection is already created";

    @Test
    public void setPropertiesWhenPropertiesFieldIsNullThenSuccessfully() throws NoSuchFieldException, IllegalAccessException {
        Field instance = BankRepositorySingleton.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(BankRepositorySingleton.class, null);

        Field properties = BankRepositorySingleton.class.getDeclaredField("properties");
        properties.setAccessible(true);
        properties.set(BankRepositorySingleton.class, null);

        Properties props = new Properties();
        BankRepositorySingleton.setProperties(props);

        assertNotNull(properties);
    }

    @Test
    public void setPropertiesWhenPropertiesFieldNotNullThenThrowException() throws NoSuchFieldException, IllegalAccessException {
        Field instance = BankRepositorySingleton.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(BankRepositorySingleton.class, bankRepository);

        Properties props = new Properties();
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> BankRepositorySingleton.setProperties(props));
        assertEquals(EXCEPTION_MESSAGE, exception.getMessage());
    }

    @Test
    public void isSingleton() throws PropertyVetoException {
        try (MockedStatic<DataSourceC3POSingleton> singleDS = mockStatic(DataSourceC3POSingleton.class)) {

            singleDS.when(DataSourceC3POSingleton::getInstance).thenReturn(dataSourceWrapper);

            IBankRepository instance1 = BankRepositorySingleton.getInstance();
            IBankRepository instance2 = BankRepositorySingleton.getInstance();


            assertNotNull(instance1);
            assertNotNull(instance2);
            assertSame(instance1, instance2);
        }
    }
}