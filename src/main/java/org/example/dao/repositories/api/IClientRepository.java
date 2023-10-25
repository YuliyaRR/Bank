package org.example.dao.repositories.api;

import org.example.dao.entity.ClientEntity;

import java.util.List;
import java.util.UUID;

public interface IClientRepository {
    List<ClientEntity> getAllClients();
    void saveClient (ClientEntity client);
    void updateClient(UUID id, ClientEntity clientEntity);
    void deleteClient(UUID id);
    boolean containsClientWithUUID(UUID id);
}
