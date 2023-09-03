package org.example.dao.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.example.dao.ds.api.IDataSourceWrapper;
import org.example.dao.entity.BankEntity;
import org.example.dao.repositories.api.IBankRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

@RequiredArgsConstructor
public class BankRepository implements IBankRepository {
    private final IDataSourceWrapper dataSource;
    private final Properties properties;
    private final static String GET_BANK_BY_ACCOUNT = "SQL_SELECT_BANK_BY_ACCOUNT";

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
}
