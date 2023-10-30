package org.example.dao.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.example.dao.ds.api.IDataSourceWrapper;
import org.example.dao.entity.ClientEntity;
import org.example.dao.repositories.api.IClientRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@RequiredArgsConstructor
public class ClientRepository implements IClientRepository {
    private final IDataSourceWrapper dataSource;
    private final Properties properties;

    private final static String SELECT_ALL_CLIENTS = "SQL_SELECT_ALL_CLIENTS";
    private final static String INSERT_NEW_CLIENT = "SQL_INSERT_NEW_CLIENT";
    private final static String UPDATE_CLIENT = "SQL_UPDATE_CLIENT";
    private final static String DELETE_CLIENT = "SQL_DELETE_CLIENT";
    private final static String SELECT_CLIENT_BY_UUID = "SQL_SELECT_CLIENT_BY_ID";

    /**
     * Метод возвращает информацию обо всех клиентах
     * @return список клиентов
     */
    @Override
    public List<ClientEntity> getAllClients() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(SELECT_ALL_CLIENTS))) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<ClientEntity> entities = new ArrayList<>();

            while (resultSet.next()) {
                UUID id = resultSet.getObject("id", UUID.class);
                String name = resultSet.getString("name");

                entities.add(new ClientEntity(id, name));
            }

            return entities;
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }

    /**
     * Метод сохраняет нового клиента в БД
     * @param client клиент для сохранения в БД
     */
    @Override
    public void saveClient(ClientEntity client) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(INSERT_NEW_CLIENT))) {

            preparedStatement.setObject(1, client.getId());
            preparedStatement.setString(2, client.getName());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }

    /**
     * Метод обновляет информацию о клиенте
     * @param id идентификатор клиента, информация о котором будет обновлена
     * @param clientEntity объект-источник информации о новом клиенте, на данный момент это новое имя
     */
    @Override
    public void updateClient(UUID id, ClientEntity clientEntity) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(UPDATE_CLIENT))) {

            preparedStatement.setString(1, clientEntity.getName());
            preparedStatement.setObject(2, id);

            int update = preparedStatement.executeUpdate();

            if(update != 1) {
                throw new RuntimeException("Something went wrong. New information wasn't saved");// TODO custom exception
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }

    /**
     * Метод удаляет клиента из БД
     * @param id идентификатор клиента, который будет удален
     */
    @Override
    public void deleteClient(UUID id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(DELETE_CLIENT))) {

            preparedStatement.setObject(1, id);

            int update = preparedStatement.executeUpdate();

            if(update != 1) {
                throw new RuntimeException("Something went wrong. Client wasn't deleted.");// TODO custom exception
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }

    @Override
    public boolean containsClientWithUUID(UUID id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(SELECT_CLIENT_BY_UUID))) {

            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }
}
