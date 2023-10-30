package org.example.service.factory;

import org.example.dao.repositories.factory.ClientRepositorySingleton;
import org.example.dao.repositories.impl.ClientRepository;
import org.example.service.api.IClientService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

public class ClientServiceSingletonTest {
    private ClientRepository clientRepository;

    @Test
    public void isSingleton() {
        try (MockedStatic<ClientRepositorySingleton> singleClientRepo = mockStatic(ClientRepositorySingleton.class)) {

            singleClientRepo.when(ClientRepositorySingleton::getInstance).thenReturn(clientRepository);

            IClientService instance1 = ClientServiceSingleton.getInstance();
            IClientService instance2 = ClientServiceSingleton.getInstance();

            assertNotNull(instance1);
            assertNotNull(instance2);
            assertSame(instance1, instance2);
        }
    }
}