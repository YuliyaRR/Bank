package org.example.dao.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.dto.Period;
import org.example.dao.repositories.api.ITransactionRepository;
import org.example.dao.ds.api.IDataSourceWrapper;
import org.example.dao.entity.TransactionEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@RequiredArgsConstructor
public class TransactionRepository implements ITransactionRepository {
    private final IDataSourceWrapper dataSource;
    private final Properties properties;
    private final static String SAVE_TRANSACTION = "SQL_SAVE_TRANSACTION";
    private final static String SAVE_ACCOUNT_TRANSACTION = "SQL_SAVE_ACCOUNT_TRANSACTION";
    private final static String SELECT_TRANSACTIONS_BY_ACCOUNT = "SQL_SELECT_TRANSACTIONS_BY_ACCOUNT";

    /** Метод сохраняет информацию в БД о преведенных транзакциях
     * @param entity энтити, которую нужно сохранить
     */
    @Override
    public void saveTransaction(TransactionEntity entity) {
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

    /**
     * Метод сохраняет информацию в БД о преведенных ежемесячных начислениях процентов
     * @param entities Список транзакций (ежемесячные начисленные проценты), которые необходимо сохранить в БД
     */
    @Override
    public void saveMonthlyInterestTransactions(List<TransactionEntity> entities) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatementTransaction = connection.prepareStatement(properties.getProperty(SAVE_TRANSACTION));
            PreparedStatement preparedStatementAccountTransaction = connection.prepareStatement(properties.getProperty(SAVE_ACCOUNT_TRANSACTION))) {
            try {
                connection.setAutoCommit(false);//transaction begin

                for (TransactionEntity transaction : entities) {
                    UUID id_transaction = transaction.getId();

                    preparedStatementTransaction.setObject(1, id_transaction);
                    preparedStatementTransaction.setString(2, transaction.getType());
                    preparedStatementTransaction.setString(3, transaction.getCurrency());
                    preparedStatementTransaction.setObject(4, transaction.getDate());

                    preparedStatementTransaction.addBatch();

                    preparedStatementAccountTransaction.setObject(1, transaction.getAccountTo());
                    preparedStatementAccountTransaction.setObject(2, id_transaction);
                    preparedStatementAccountTransaction.setDouble(3, transaction.getSum());

                    preparedStatementAccountTransaction.addBatch();
                }

                preparedStatementTransaction.executeBatch();
                preparedStatementAccountTransaction.executeBatch();

                connection.commit();//transaction end

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Something went wrong. Transaction commit error", e); //TODO custom exception
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);//TODO custom exception
        }
    }

    /** Метод предоставляет список всех транзакций по счету
     * @param account счет, по которому нужно найти транзакции
     * @param period период, за который ябудет сформирован список транзакций
     * @return список всех транзакций по счету. Если транзакций по счету нет, то возвращается пустой список
     */
    @Override
    public List<TransactionEntity> allAccountTransactions(UUID account, Period period) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(SELECT_TRANSACTIONS_BY_ACCOUNT))) {

            preparedStatement.setObject(1, account);
            preparedStatement.setObject(2, period.getDateFrom());
            preparedStatement.setObject(3, period.getDateTo().plusDays(1));

            ResultSet resultSet = preparedStatement.executeQuery();

            List<TransactionEntity> entities = new ArrayList<>();
            TransactionEntity.TransactionEntityBuilder builder = TransactionEntity.builder();

            while (resultSet.next()) {
                TransactionEntity entity = builder.setDate(resultSet.getObject("date", LocalDateTime.class))
                        .setType(resultSet.getString("name_transaction_type"))
                        .setSum(resultSet.getDouble("sum"))
                        .build();

                entities.add(entity);
            }

            return entities;
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }
}
