package org.example.dao.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.example.dao.ds.api.IDataSourceWrapper;
import org.example.dao.entity.BankEntity;
import org.example.dao.repositories.api.IBankRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@RequiredArgsConstructor
public class BankRepository implements IBankRepository {
    private final IDataSourceWrapper dataSource;
    private final Properties properties;
    private final static String GET_BANK_BY_ACCOUNT = "SQL_SELECT_BANK_BY_ACCOUNT";
    private final static String SELECT_ALL_BANKS = "SQL_SELECT_ALL_BANKS";
    private final static String INSERT_NEW_BANK = "SQL_INSERT_NEW_BANK";
    private final static String UPDATE_BANK = "SQL_UPDATE_BANK";
    private final static String DELETE_BANK = "SQL_DELETE_BANK";

    /**
     * Метод предоставляет информацию о банке, в котором открыт счет
     * @param account счет
     * @return BankEntity, содержит информацию о банке, в котором открыт счет
     * В случае невозможности найти банк, которому принадлежит переданный счет, выбрасывается RuntimeException
     */
    @Override
    public BankEntity getBankByAccount(UUID account) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(GET_BANK_BY_ACCOUNT))) {

            preparedStatement.setObject(1, account);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                UUID id = resultSet.getObject("id", UUID.class);
                String name = resultSet.getString("name");

                return new BankEntity(id, name);

            } else {
                throw new RuntimeException("The bank that owns this account was not found");//TODO custom exception
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }

    /**
     * Метод возвращает информацию обо всех банках
     * @return список банков
     */
    @Override
    public List<BankEntity> getAllBanks() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(SELECT_ALL_BANKS))) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<BankEntity> entities = new ArrayList<>();

            while (resultSet.next()) {
                UUID id = resultSet.getObject("id", UUID.class);
                String name = resultSet.getString("name");

                entities.add(new BankEntity(id, name));
            }

            return entities;
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }

    /**
     * Метод сохраняет новый банк в БД
     * @param bank банк для сохранения в БД
     */
    @Override
    public void saveBank(BankEntity bank) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(INSERT_NEW_BANK))) {

            preparedStatement.setObject(1, bank.getId());
            preparedStatement.setString(2, bank.getName());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }

    /**
     * Метод обновляет информацию о банке
     * @param uuid идентификатор банка, информация о котором будет обновлена
     * @param entity объект-источник информации о новом банке, на данный момент это новое имя банка
     */
    @Override
    public void updateBank(UUID uuid, BankEntity entity) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(UPDATE_BANK))) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setObject(2, uuid);

            int update = preparedStatement.executeUpdate();

            if(update != 1) {
                throw new RuntimeException("Something went wrong. New information wasn't saved.");// TODO custom exception
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }

    /**
     * Метод удаляет банк из БД
     * @param uuid идентификатор банка, который будет удален
     */
    @Override
    public void deleteBank(UUID uuid) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(DELETE_BANK))) {

            preparedStatement.setObject(1, uuid);

            int update = preparedStatement.executeUpdate();

            if(update != 1) {
                throw new RuntimeException("Something went wrong. Bank wasn't deleted.");// TODO custom exception
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }
}
