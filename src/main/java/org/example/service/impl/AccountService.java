package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.dto.Currency;
import org.example.core.dto.Transaction;
import org.example.core.dto.TransactionType;
import org.example.dao.entity.AccountEntity;
import org.example.dao.repositories.api.IAccountRepository;
import org.example.service.api.IAccountService;
import org.example.service.api.ITransactionService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final IAccountRepository accountRepository;
    private final ITransactionService transactionService;
    private  boolean isItTimeToCalculateTheMonthlyInterest = false;

    /**
     * Метод предназначен для проведения операции по пополнению счета наличными.
     * Тип транзакции устанавливается в TransactionType.CASH_REPLENISHMENT.
     * @param accountTo  счет, на который будут зачислены денежные средства
     * @param sum  сумма к зачислению
     * @param currency  валюта операции
     */
    @Override
    public void addMoney(UUID accountTo, double sum, Currency currency) {
        AccountEntity accountEntity = accountRepository.checkAccount(accountTo);
        checkAccountCurrency(accountEntity, currency);

        TransactionType transactionType = TransactionType.CASH_REPLENISHMENT;

        Transaction transaction = Transaction.builder()
                .setId(UUID.randomUUID())
                .setCurrency(currency)
                .setAccountTo(accountTo)
                .setSum(sum)
                .setType(transactionType)
                .build();

        accountRepository.updateBalanceCashOperation(transaction);

    }

    /**
     * Метод предназначен проведения операции по снятию денежных средств со счета.
     * Тип транзакции устанавливается в TransactionType.WITHDRAWALS.
     * @param accountFrom  счет, с которого будут сняты денежные средства
     * @param sum сумма к списанию
     * @param currency  валюта операции
     */
    @Override
    public void withdrawalMoney(UUID accountFrom, double sum, Currency currency) {
        AccountEntity accountEntity = accountRepository.checkAccount(accountFrom);

        checkAccountCurrency(accountEntity, currency);
        checkAccountBalance(accountEntity, sum);

        sum = -sum;
        TransactionType transactionType = TransactionType.WITHDRAWALS;

        Transaction transaction = Transaction.builder()
                .setId(UUID.randomUUID())
                .setCurrency(currency)
                .setAccountFrom(accountFrom)
                .setSum(sum)
                .setType(transactionType)
                .build();

        accountRepository.updateBalanceCashOperation(transaction);
    }

    /**
     * Метод предназначен для проведения операций между банковскими счетами, открытыми как в одном банке, так и в различных.
     * Поддерживаемые типы транзакций: TransactionType.WAGE, TransactionType.MONEY_TRANSFER, TransactionType.DEPOSIT_INTEREST,
     * TransactionType.PAYMENT_FOR_SERVICES
     * @param transaction объект, содержащий детали проводимой операции,
     *                    в частности: - счета, между которыми будет осуществлено списание/зачисление денежных средств,
     *                                 - сумма операции,
     *                                 - тип проводимой транзакции,
     *                                 - валюта операции
     */
    @Override
    public void transferMoney(Transaction transaction) {
        Currency currency = transaction.getCurrency();
        double sum = transaction.getSum();
        UUID accountFrom = transaction.getAccountFrom();
        UUID accountTo = transaction.getAccountTo();

        AccountEntity accountEntityFrom = accountRepository.checkAccount(accountFrom);
        checkAccountCurrency(accountEntityFrom, currency);
        checkAccountBalance(accountEntityFrom, sum);

        AccountEntity accountEntityTo = accountRepository.checkAccount(accountTo);
        checkAccountCurrency(accountEntityTo, currency);

        transaction.setId(UUID.randomUUID());

        accountRepository.updateBalanceCashlessPayments(transaction);
    }

    @Override
    public void checkTheNeedToCalculateInterest() {
        LocalDateTime now = LocalDateTime.now();
        LocalTime localTime = now.toLocalTime();

        LocalDate localDate = now.toLocalDate();
        LocalTime expected = LocalTime.of(23, 59, 30);

        int lengthOfMonth = localDate.lengthOfMonth();
        int dayOfMonth = localDate.getDayOfMonth();

        if(lengthOfMonth == dayOfMonth) {
            if(localTime.equals(expected) || localTime.isAfter(expected)) {
                if(!isItTimeToCalculateTheMonthlyInterest) {
                    calculateInterest();
                    isItTimeToCalculateTheMonthlyInterest = true;
                }
            }
        } else {
            isItTimeToCalculateTheMonthlyInterest = false;
        }
    }

    @Override
    public void calculateInterest() {
        accountRepository.calculateMonthlyInterest();
    }

    private void checkAccountCurrency(AccountEntity entity, Currency currency) {
        if (!entity.getCurrency().equals(currency.name())){
            throw new RuntimeException("Payment currency doesn't match with account currency");//TODO custom exception
        }
    }

    private void checkAccountBalance(AccountEntity accountEntity, double sum) {
        if(accountEntity.getBalance() < sum) {
            throw new RuntimeException("You don't have enough money for this operation");//TODO custom exception
        }
    }
}
