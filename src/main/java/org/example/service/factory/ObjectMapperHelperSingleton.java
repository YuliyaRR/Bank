package org.example.service.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectMapperHelperSingleton {
    private volatile static ObjectMapper mapper;

    private ObjectMapperHelperSingleton() {
    }

    public static ObjectMapper getObjectMapper() {
        if (mapper == null) {
            synchronized (ObjectMapperHelperSingleton.class) {
                if (mapper == null) {
                    mapper = new ObjectMapper(new YAMLFactory());
                    mapper.registerModule(new JavaTimeModule());
                    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
                }
            }
        }
        return mapper;
    }
}
