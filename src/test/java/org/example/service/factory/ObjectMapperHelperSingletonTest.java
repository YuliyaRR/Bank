package org.example.service.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectMapperHelperSingletonTest {
    @Test
    public void checkPropertyOfObjectMapper() {
        ObjectMapper mapper = ObjectMapperHelperSingleton.getObjectMapper();
        assertNotNull(mapper);
        assertFalse(mapper.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
    }

    @Test
    public void isSingleton() {
        ObjectMapper mapper1 = ObjectMapperHelperSingleton.getObjectMapper();
        ObjectMapper mapper2 = ObjectMapperHelperSingleton.getObjectMapper();

        assertNotNull(mapper1);
        assertNotNull(mapper2);
        assertSame(mapper1, mapper2);
    }
}