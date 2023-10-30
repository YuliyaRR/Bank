package org.example.service.impl;

import org.example.core.dto.Client;
import org.example.dao.entity.ClientEntity;
import org.example.dao.repositories.api.IClientRepository;
import org.example.service.api.IClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private IClientRepository clientRepository;
    private IClientService clientService;
    private final static UUID NUM_CLIENT_EXISTS = UUID.fromString("12605bea-4688-4a8a-b64f-d29e24eb6d81");
    private final static UUID NUM_CLIENT_NOT_EXISTS = UUID.fromString("12605bea-4688-4a8a-b64f-d29e24eb6d81");
    private final static String EXCEPTION_NO_CLIENTS_IN_SYSTEM = "There are no clients registered in the system";
    private final static String EXCEPTION_CLIENT_NOT_FOUND = "Client not found";


    @BeforeEach
    public void setUp() {
        this.clientService = new ClientService(clientRepository);
    }

    @Test
    public void getAllClientsWhenTheyAreRegisteredInSystem() {
        ClientEntity clientEntityA = new ClientEntity(UUID.randomUUID(), "Client 1");
        ClientEntity clientEntityB = new ClientEntity(UUID.randomUUID(), "Client 2");
        ClientEntity clientEntityC = new ClientEntity(UUID.randomUUID(), "Client 3");

        List<ClientEntity> fromDaoList = new ArrayList<>(List.of(clientEntityA, clientEntityB, clientEntityC));

        when(clientRepository.getAllClients()).thenReturn(fromDaoList);

        List<Client> allClients = clientService.getAllClients();

        List<Client> expected = fromDaoList.stream()
                .map(entity -> new Client(entity.getId(), entity.getName()))
                .toList();

        assertNotNull(allClients);
        assertFalse(allClients.isEmpty());
        assertEquals(expected, allClients);

        verify(clientRepository, times(1)).getAllClients();
    }

    @Test
    public void getAllClientsWhenNoClientsInSystemThenThrowException() {
        List<ClientEntity> fromDaoList = new ArrayList<>();

        when(clientRepository.getAllClients()).thenReturn(fromDaoList);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> clientService.getAllClients());
        assertEquals(EXCEPTION_NO_CLIENTS_IN_SYSTEM, exception.getMessage());

        verify(clientRepository, times(1)).getAllClients();
    }

    @Test
    public void createClient() {
        Client client = new Client(UUID.randomUUID(), "Client 1");

        clientService.createClient(client);

        verify(clientRepository, times(1)).saveClient(any(ClientEntity.class));
    }

    @Test
    public void updateClientWhenClientIsFoundThenUpdateIsSuccessful() {
        when(clientRepository.containsClientWithUUID(NUM_CLIENT_EXISTS)).thenReturn(true);

        clientService.updateClient(NUM_CLIENT_EXISTS, new Client(NUM_CLIENT_EXISTS, "New Name Client"));

        verify(clientRepository, times(1)).containsClientWithUUID(NUM_CLIENT_EXISTS);
        verify(clientRepository, times(1)).updateClient(any(), any(ClientEntity.class));
    }

    @Test
    public void updateClientWhenClientNotFoundThenThrowException() {
        when(clientRepository.containsClientWithUUID(NUM_CLIENT_NOT_EXISTS)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> clientService.updateClient(NUM_CLIENT_NOT_EXISTS, new Client(NUM_CLIENT_NOT_EXISTS, "New Name Client")));
        assertEquals(EXCEPTION_CLIENT_NOT_FOUND, exception.getMessage());

        verify(clientRepository, times(1)).containsClientWithUUID(NUM_CLIENT_NOT_EXISTS);
        verify(clientRepository, times(0)).updateClient(any(), any(ClientEntity.class));
    }

    @Test
    public void deleteClientWhenClientIsFoundThenUpdateIsSuccessful(){
        when(clientRepository.containsClientWithUUID(NUM_CLIENT_EXISTS)).thenReturn(true);

        clientService.deleteClient(NUM_CLIENT_EXISTS);

        verify(clientRepository, times(1)).containsClientWithUUID(NUM_CLIENT_EXISTS);
        verify(clientRepository, times(1)).deleteClient(NUM_CLIENT_EXISTS);
    }

    @Test
    public void deleteClientWhenClientNotFoundThenThrowException() {
        when(clientRepository.containsClientWithUUID(NUM_CLIENT_NOT_EXISTS)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> clientService.deleteClient(NUM_CLIENT_NOT_EXISTS));
        assertEquals(EXCEPTION_CLIENT_NOT_FOUND, exception.getMessage());

        verify(clientRepository, times(1)).containsClientWithUUID(NUM_CLIENT_NOT_EXISTS);
        verify(clientRepository, times(0)).deleteClient(any());
    }
}
