package org.example.service.api;

import org.example.core.dto.Client;

import java.util.List;
import java.util.UUID;

public interface IClientService {
    List<Client> getAllClients();
    void createClient(Client client);
    void updateClient(UUID id, Client client);
    void deleteClient(UUID id);

}
