package org.example.dao.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.example.dao.repositories.api.ITransactionRepository;
import org.example.dao.ds.api.IDataSourceWrapper;
import org.example.dao.entity.TransactionEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

@RequiredArgsConstructor
public class TransactionRepository implements ITransactionRepository {
    private final IDataSourceWrapper dataSource;
    private final Properties properties;
    private final static String SAVE_TRANSACTION = "SQL_SAVE_TRANSACTION";
    private final static String SAVE_ACCOUNT_TRANSACTION = "SQL_SAVE_ACCOUNT_TRANSACTION";
    @Override
    public void save(TransactionEntity entity) {
        UUID id_transaction = entity.getId();

        try(Connection connection = dataSource.getConnection()) {
            try {
                connection.setAutoCommit(false);//transaction begin

                PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(SAVE_TRANSACTION));

                preparedStatement.setObject(1, id_transaction);
                preparedStatement.setString(2, entity.getType());
                preparedStatement.setString(3, entity.getCurrency());
                preparedStatement.setObject(4, entity.getDate());

                preparedStatement.executeUpdate();

                double sum = Math.abs(entity.getSum());

                preparedStatement = connection.prepareStatement(properties.getProperty(SAVE_ACCOUNT_TRANSACTION));
                UUID accountFrom = entity.getAccountFrom();
                UUID accountTo = entity.getAccountTo();

                if (accountFrom != null) {
                    preparedStatement.setObject(1, accountFrom);
                    preparedStatement.setObject(2, id_transaction);
                    preparedStatement.setDouble(3, -sum);
                    preparedStatement.executeUpdate();
                }

                if (accountTo != null) {
                    preparedStatement.setObject(1, accountTo);
                    preparedStatement.setObject(2, id_transaction);
                    preparedStatement.setDouble(3, sum);
                    preparedStatement.executeUpdate();
                }

                connection.commit();//transaction end

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Something went wrong. Transaction commit error", e); //TODO custom exception
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);//TODO custom exception
        }
    }
}
