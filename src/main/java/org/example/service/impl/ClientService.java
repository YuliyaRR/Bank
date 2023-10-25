package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.dto.Client;
import org.example.dao.entity.ClientEntity;
import org.example.dao.repositories.api.IClientRepository;
import org.example.service.api.IClientService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ClientService implements IClientService {
    private final IClientRepository clientRepository;

    /**
     * Метод возвращает информацию обо всех клиентах, зарегистрированных в системе
     * @return список клиентов
     */
    @Override
    public List<Client> getAllClients() {
        List<ClientEntity> allClients = clientRepository.getAllClients();

        if(allClients.isEmpty()) {
            throw new RuntimeException("There are no clients registered in the system");
        }

        return allClients.stream()
                .map(clientEntity -> convertEntityToDto(clientEntity))
                .toList();
    }

    /** Метод передает информацию о клиенте для сохранения в БД
     * @param client объект-источник информации о новом клиенте, на данный момент это имя
     */
    @Override
    public void createClient(Client client) {
        clientRepository.saveClient(new ClientEntity(UUID.randomUUID(), client.getName()));
    }

    /**
     * Метод передает данные для обновления информации о клиенте на БД
     * @param uuid идентификатор клиента, информация о котором будет обновлена
     * @param client объект-источник информации о новом клиенте, на данный момент это новое имя
     */
    @Override
    public void updateClient(UUID uuid, Client client) {
        checkClientExistenceByUUID(uuid);
        clientRepository.updateClient(uuid, new ClientEntity(uuid, client.getName()));
    }

    /**
     * Метод передает данные для удаления клиента из БД
     * @param uuid идентификатор клиента, который будет удален
     */
    @Override
    public void deleteClient(UUID uuid) {
        checkClientExistenceByUUID(uuid);
        clientRepository.deleteClient(uuid);
    }

    /** Метод проверяет существует ли клиент с переданным идентификатором
     * @param uuid идентификатор клиента
     */
    private void checkClientExistenceByUUID(UUID uuid) {
        if (!clientRepository.containsClientWithUUID(uuid)) {
            throw new RuntimeException("Client not found");
        }
    }

    /** Мeтод конвертирует ClientEntity в Client
     * @param entity объект-источник
     * @return Client
     */
    private Client convertEntityToDto(ClientEntity entity) {
        return new Client(entity.getId(), entity.getName());
    }
}
