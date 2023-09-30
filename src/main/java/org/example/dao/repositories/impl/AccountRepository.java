package org.example.dao.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.dto.Currency;
import org.example.core.dto.Transaction;
import org.example.core.dto.TransactionType;
import org.example.core.events.TransactionEvent;
import org.example.dao.entity.AccountEntity;
import org.example.dao.entity.BankEntity;
import org.example.dao.entity.ClientEntity;
import org.example.dao.repositories.api.IAccountRepository;
import org.example.dao.ds.api.IDataSourceWrapper;
import org.example.listener.api.IPublisher;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
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
    private final static String SELECT_ACCOUNT_BY_UUID = "SQL_SELECT_ACCOUNT_BY_UUID";
    private final static String MONTHLY_INTEREST = "MONTHLY_INTEREST";
    private final static String BANK_NAME = "BANK_NAME";

    /**
     * Метод проверяет есть ли аккаунт с таким идентификатором
     * @param account UUID проверяемого аккаунта
     * @return - AccountEntity с полями баланс и тип валюты,
     * если аккаунт не найден - выбрасывается исключение RuntimeException
     */
    @Override
    public AccountEntity checkAccountExistence(UUID account) {

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
     * Если обновление проходит неуспешно, то выбрасывается исключение RuntimeException
     */
    @Override
    public Transaction updateBalanceCashOperation(Transaction transaction) {
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

                return transaction;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }

    /**
     * Метод проводит транзакцию между двумя счетами, обновляя балансы в обоих, даже если один из счетов открыт в другом банке
     * @param transaction объект, содержащий детали проводимой операции
     * Если обновление проходит неуспешно, то выбрасывается исключение RuntimeException
     * @return объект transactionб дополненный информацией о дате операции
     */
    @Override
    public Transaction updateBalanceCashlessPayments(Transaction transaction) {
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

                return transaction;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }

    /**
     * Метод производит расчет и начисление процентов по остаткам на счетах в конце месяца.
     * Начисление происходит только по счетам, принадлежащим банку Clever-Bank
     */
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
                    UUID num = resultSet.getObject("num", UUID.class);
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

    /**
     * Метод предоставляет полную информацию по открытому счету
     * @param uuid счет, по которому запрашивается информация
     * @return - AccountEntity, содержит всю информацию по счету,
     * если аккаунт не найден - выбрасывается исключение RuntimeException
     */
    @Override
    public AccountEntity getAccountInfo(UUID uuid) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(SELECT_ACCOUNT_BY_UUID))) {

            preparedStatement.setObject(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                return AccountEntity.builder()
                        .setNum(uuid)
                        .setCurrency(resultSet.getString("name_currency"))
                        .setBank(new BankEntity(resultSet.getObject("id_bank", UUID.class), resultSet.getString("name_bank")))
                        .setDateOpen(resultSet.getObject("date_open", LocalDate.class))
                        .setDateLastTransaction(resultSet.getObject("date_last_transaction", LocalDateTime.class))
                        .setBalance(resultSet.getDouble("balance"))
                        .setOwner(new ClientEntity(resultSet.getObject("id_client", UUID.class), resultSet.getString("name_client")))
                        .build();

            } else {
                throw new RuntimeException("Account not found");//TODO custom exception
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);// TODO custom exception
        }
    }
}
