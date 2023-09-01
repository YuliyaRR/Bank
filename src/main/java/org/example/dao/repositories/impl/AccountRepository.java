package org.example.dao.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.dto.Currency;
import org.example.core.dto.Transaction;
import org.example.core.dto.TransactionType;
import org.example.core.events.TransactionEvent;
import org.example.dao.entity.AccountEntity;
import org.example.dao.repositories.api.IAccountRepository;
import org.example.dao.ds.api.IDataSourceWrapper;
import org.example.listener.IPublisher;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
public class AccountRepository implements IAccountRepository {
    private final IDataSourceWrapper dataSource;
    private final Properties properties;
    private final IPublisher<TransactionEvent> publisher;
    private final static String CHECK_ACCOUNT = "SQL_CHECK_ACCOUNT";
    private final static String CASH_UPDATE_BALANCE_WITH_CHECK = "SQL_CASH_UPDATE_BALANCE_WITH_CHECK";
    private final static String CASH_UPDATE_BALANCE = "SQL_CASH_UPDATE_BALANCE";
    private final static String CASHLESS_UPDATE_BALANCE = "SQL_CASHLESS_UPDATE_BALANCE";
    private final static String SELECT_ACCOUNTS_BEFORE_INTEREST_PAYMENT = "SQL_SELECT_ACCOUNTS_BEFORE_INTEREST_PAYMENT";
    private final static String MONTHLY_INTEREST_PAYMENT = "SQL_MONTHLY_INTEREST_PAYMENT";
    private final static String MONTHLY_INTEREST = "MONTHLY_INTEREST";
    private final static String BANK_NAME = "BANK_NAME";

    @Override
    public AccountEntity checkAccount(UUID account) {

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(CHECK_ACCOUNT))) {

            preparedStatement.setObject(1, account);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                String currency = resultSet.getString("name_currency");

                return AccountEntity.builder().setBalance(balance).setCurrency(currency).build();

            } else {
                throw new RuntimeException("Account not found");//TODO custom exception
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }

    /**
     * Метод обновляет баланс по счету клиента после пополнения наличными или снятия наличных со счета
     * @param transaction объект, содержащий детали проводимой операции
     */
    @Override
    public void updateBalanceCashOperation(Transaction transaction) {
        double sum = transaction.getSum();
        UUID account;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);//transaction begin

            PreparedStatement preparedStatement;

            if (sum >= 0) {
                account = transaction.getAccountTo();
                preparedStatement = connection.prepareStatement(properties.getProperty(CASH_UPDATE_BALANCE));
            } else {
                account = transaction.getAccountFrom();
                preparedStatement = connection.prepareStatement(properties.getProperty(CASH_UPDATE_BALANCE_WITH_CHECK));
                preparedStatement.setDouble(4, sum);
            }

            LocalDateTime dateTime = LocalDateTime.now();

            preparedStatement.setDouble(1, sum);
            preparedStatement.setObject(2, dateTime);
            preparedStatement.setObject(3, account);

            int update = preparedStatement.executeUpdate();

            if (update == 0) {
                connection.rollback();
                throw new RuntimeException("Something went wrong. Check your balance and account and try again.");// TODO custom exception
            } else {
                transaction.setDate(dateTime);
                publisher.notify(new TransactionEvent(transaction));
                connection.commit();//transaction end
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }

    @Override
    public void updateBalanceCashlessPayments(Transaction transaction) {
        UUID accountFrom = transaction.getAccountFrom();
        UUID accountTo = transaction.getAccountTo();
        double sum = transaction.getSum();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(CASHLESS_UPDATE_BALANCE))) {

            connection.setAutoCommit(false);//transaction begin

            preparedStatement.setObject(1, accountFrom);
            preparedStatement.setDouble(2, sum);
            preparedStatement.setObject(3, accountTo);
            preparedStatement.setDouble(4, sum);

            LocalDateTime dateTime = LocalDateTime.now();

            preparedStatement.setObject(5, dateTime);
            preparedStatement.setDouble(6, sum);
            preparedStatement.setObject(7, accountFrom);

            preparedStatement.setObject(8, accountFrom);
            preparedStatement.setObject(9, accountTo);

            int update = preparedStatement.executeUpdate();

            if (update < 2) {
                connection.rollback();
                throw new RuntimeException("Something went wrong. Check your balance and account and try again.");// TODO custom exception
            } else {
                transaction.setDate(dateTime);
                publisher.notify(new TransactionEvent(transaction));
                connection.commit();//transaction end
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }

    @Override
    public void calculateMonthlyInterest() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatementSelect = connection.prepareStatement(properties.getProperty(SELECT_ACCOUNTS_BEFORE_INTEREST_PAYMENT));
             PreparedStatement preparedStatementUpdate = connection.prepareStatement(properties.getProperty(MONTHLY_INTEREST_PAYMENT))) {

            List<Transaction> transactionList = new ArrayList<>();

            try {
                connection.setAutoCommit(false);//transaction begin

                preparedStatementSelect.setString(1, properties.getProperty(BANK_NAME));
                ResultSet resultSet = preparedStatementSelect.executeQuery();

                LocalDateTime localDateTime = LocalDateTime.now();
                TransactionType depositInterest = TransactionType.DEPOSIT_INTEREST;

                Transaction.TransactionBuilder builder = Transaction.builder();
                double interest;

                while (resultSet.next()){
                    UUID num = (UUID) resultSet.getObject("num");
                    Currency currency = Currency.valueOf(resultSet.getString("name_currency"));
                    double balance = resultSet.getDouble("balance");

                    interest = BigDecimal.valueOf(balance * Double.parseDouble(properties.getProperty(MONTHLY_INTEREST)))
                            .setScale(2, RoundingMode.HALF_UP).doubleValue();

                    transactionList.add(
                            builder.setId(UUID.randomUUID())
                            .setCurrency(currency)
                            .setDate(localDateTime)
                            .setAccountTo(num)
                            .setSum(interest)
                            .setType(depositInterest)
                            .build()
                    );

                    preparedStatementUpdate.setDouble(1, interest + balance);
                    preparedStatementUpdate.setObject(2, localDateTime);
                    preparedStatementUpdate.setObject(3, num);

                    preparedStatementUpdate.addBatch();

                }

                preparedStatementUpdate.executeBatch();

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Something went wrong. Transaction commit error", e); //TODO custom exception
            }

            publisher.notify(
                    transactionList.stream()
                            .map(transaction -> new TransactionEvent(transaction))
                            .toList()
            );

            connection.commit();//transaction end

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }
}
