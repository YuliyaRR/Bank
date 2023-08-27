package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.dto.Currency;
import org.example.core.dto.Transaction;
import org.example.core.dto.TransactionType;
import org.example.dao.api.IAccountRepository;
import org.example.dao.entity.AccountEntity;
import org.example.service.api.IAccountService;
import org.example.service.api.ITransactionService;

import java.util.UUID;
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final IAccountRepository accountRepository;
    private final ITransactionService transactionService;

    /**
     * Метод предназначен для проведения операции по пополнению счета наличными.
     * Тип транзакции устанавливается в TransactionType.CASH_REPLENISHMENT.
     * @param accountTo  счет, на который будут зачислены денежные средства
     * @param sum  сумма к зачислению
     */
    @Override
    public void addMoney(UUID accountTo, double sum) {
        checkAccountExistence(accountTo);

        AccountEntity accountEntity = accountRepository.updateBalance(accountTo, sum);
        TransactionType transactionType = TransactionType.CASH_REPLENISHMENT;

        Transaction transaction = Transaction.builder()
                .setId(UUID.randomUUID())
                .setCurrency(Currency.valueOf(accountEntity.getCurrency()))
                .setDate(accountEntity.getDateLastTransaction())
                .setAccountTo(accountTo)
                .setSum(sum)
                .setType(transactionType)
                .build();

        transactionService.saveTransaction(transaction);
    }

    /**
     * Метод предназначен проведения операции по снятию денежных средств со счета.
     * Тип транзакции устанавливается в TransactionType.WITHDRAWALS.
     * @param accountFrom  счет, с которого будут сняты денежные средства
     * @param sum сумма к списанию
     */
    @Override
    public void withdrawalMoney(UUID accountFrom, double sum) {
        checkAccountExistence(accountFrom);
        checkAccountBalance(accountFrom, sum);

        sum = -sum;

        AccountEntity accountEntity = accountRepository.updateBalance(accountFrom, sum);
        TransactionType transactionType = TransactionType.WITHDRAWALS;

        Transaction transaction = Transaction.builder()
                .setId(UUID.randomUUID())
                .setCurrency(Currency.valueOf(accountEntity.getCurrency()))
                .setDate(accountEntity.getDateLastTransaction())
                .setAccountFrom(accountFrom)
                .setSum(sum)
                .setType(transactionType)
                .build();

        transactionService.saveTransaction(transaction);
    }

    /**
     * Метод предназначен для проведения операций между банковскими счетами, открытыми как в одном банке, так и в различных.
     * Поддерживаемые типы транзакций: TransactionType.WAGE, TransactionType.MONEY_TRANSFER, TransactionType.DEPOSIT_INTEREST,
     * TransactionType.PAYMENT_FOR_SERVICES
     * @param transaction объект, содержащий детали проводимой операции,
     *                    в частности: счета, между которыми будет осуществлено списание/зачисление денежных средств,
     *                    сумма операции, тип проводимой транзакции.
     */
    @Override
    public void transferMoney(Transaction transaction) {
        UUID accountFrom = transaction.getAccountFrom();

        checkAccountExistence(accountFrom);
        checkAccountExistence(transaction.getAccountTo());

        checkAccountBalance(accountFrom, transaction.getSum());

    }

    private void checkAccountExistence(UUID account) {
        if (!accountRepository.doesExist(account)){
            throw new RuntimeException("Account not found");//TODO custom exception
        }
    }

    private void checkAccountBalance(UUID account, double sum) {
        if(!accountRepository.doesEnoughMoney(account, sum)) {
            throw new RuntimeException("You don't have enough money for this operation");//TODO custom exception
        }
    }
}
