package org.example.service.factory;

import org.example.service.api.IDocCreationService;
import org.example.service.impl.DocCreationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.beans.PropertyVetoException;
import java.lang.reflect.Field;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
public class DocCreationServiceSingletonTest {
    @Mock
    private DocCreationService service;
    private static final String EXCEPTION_MESSAGE = "You can't change settings if the database connection is already created";


    @Test
    public void setPropertiesWhenPropertiesFieldIsNullThenSuccessfully() throws NoSuchFieldException, IllegalAccessException {
        Field instance = DocCreationServiceSingleton.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(DocCreationServiceSingleton.class, null);

        Field properties = DocCreationServiceSingleton.class.getDeclaredField("properties");
        properties.setAccessible(true);
        properties.set(DocCreationServiceSingleton.class, null);

        Properties props = new Properties();
        DocCreationServiceSingleton.setProperties(props);

        assertNotNull(properties);
    }

    @Test
    public void setPropertiesWhenPropertiesFieldNotNullThenThrowException() throws NoSuchFieldException, IllegalAccessException {
        Field instance = DocCreationServiceSingleton.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(DocCreationServiceSingleton.class, service);

        Properties props = new Properties();
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> DocCreationServiceSingleton.setProperties(props));
        assertEquals(EXCEPTION_MESSAGE, exception.getMessage());
    }

    @Test
    public void isSingleton() throws PropertyVetoException {
        IDocCreationService instance1 = DocCreationServiceSingleton.getInstance();
        IDocCreationService instance2 = DocCreationServiceSingleton.getInstance();

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2);
    }
}