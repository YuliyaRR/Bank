package org.example.dao.impl;

import lombok.RequiredArgsConstructor;
import org.example.dao.api.IAccountRepository;
import org.example.dao.ds.api.IDataSourceWrapper;
import org.example.dao.entity.AccountEntity;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

@RequiredArgsConstructor
public class AccountRepository implements IAccountRepository {
    private final IDataSourceWrapper dataSource;
    private final Properties properties;
    private final static String DOES_ACCOUNT_EXIST = "SQL_DOES_ACCOUNT_EXIST";
    private final static String DOES_ENOUGH_MONEY = "SQL_DOES_ENOUGH_MONEY";
    private final static String CHECK_BEFORE_UPDATE_BALANCE = "SQL_CHECK_BEFORE_UPDATE_BALANCE";
    private final static String UPDATE_BALANCE = "SQL_UPDATE_BALANCE";

    @Override
    public boolean doesExist(UUID account) {
        boolean result = false;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(DOES_ACCOUNT_EXIST))) {

            preparedStatement.setObject(1, account);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);//TODO custom exception
        }

        return result;
    }

    @Override
    public boolean doesEnoughMoney(UUID account, double sum) {
        boolean result = false;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(DOES_ENOUGH_MONEY))) {

            preparedStatement.setObject(1, account);
            preparedStatement.setDouble(2, sum);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);//TODO custom exception
        }

        return result;
    }

    @Override
    public AccountEntity updateBalance(UUID account, double sum) {
        AccountEntity.AccountEntityBuilder builder = AccountEntity.builder().setNum(account);
        double balance;

        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(CHECK_BEFORE_UPDATE_BALANCE));

            preparedStatement.setObject(1, account);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                balance = resultSet.getDouble("balance");

                if(sum < 0) {
                    checkBalanceLimit(balance, sum);
                }

                String currency = resultSet.getString("name_currency");
                builder.setCurrency(currency);

            } else {
                throw new RuntimeException("Account not found");//TODO custom exception
            }

            preparedStatement = connection.prepareStatement(properties.getProperty(UPDATE_BALANCE));
            LocalDateTime dateTime = LocalDateTime.now();
            builder.setDateLastTransaction(dateTime);

            preparedStatement.setDouble(1, balance + sum);
            preparedStatement.setObject(2, dateTime);
            preparedStatement.setObject(3, account);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);//TODO custom exception
        }

        return builder.build();
    }

    private void checkBalanceLimit(double balance, double sum) {
        if (balance < Math.abs(sum)) {
            throw new RuntimeException("You don't have enough money for this operation");//TODO custom exception
        }
    }
}
