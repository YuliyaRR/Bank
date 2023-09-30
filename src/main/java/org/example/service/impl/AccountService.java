package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.dto.*;
import org.example.core.dto.docs.Check;
import org.example.core.events.CheckEvent;
import org.example.dao.entity.AccountEntity;
import org.example.dao.repositories.api.IAccountRepository;
import org.example.listener.api.IPublisher;
import org.example.service.api.IAccountService;
import org.example.service.api.IBankService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final IAccountRepository accountRepository;
    private final IBankService bankService;
    private final IPublisher<CheckEvent> publisher;
    private boolean isItTimeToCalculateTheMonthlyInterest = false;


    /**
     * Метод предназначен для проведения операции по пополнению счета наличными
     * и работает над операциями типа TransactionType.CASH_REPLENISHMENT
     * @param transaction объект, содержащий детали проводимой операции,
     *                    в частности: - счет, на который будут зачислены денежные средства,
     *                                 - сумма к зачислению,
     *                                 - тип проводимой транзакции,
     *                                 - валюта операции
     * @return объект Check, содержащий информацию о проведенной операции
     */
    @Override
    public Check addMoney(Transaction transaction) {
        if (!transaction.getType().equals(TransactionType.CASH_REPLENISHMENT)){
            throw new RuntimeException("Transaction type doesn't match the operation being performed");//TODO custom exception
        }

        checkTransactionSum(transaction.getSum());

        UUID accountTo = transaction.getAccountTo();
        AccountEntity accountEntity = accountRepository.checkAccountExistence(accountTo);

        Currency currency = transaction.getCurrency();
        checkAccountCurrency(accountEntity, currency);

        transaction.setId(UUID.randomUUID());

        Check check = convertTransactionToCheck(accountRepository.updateBalanceCashOperation(transaction));

        publisher.notify(new CheckEvent(check));

        check.setBankFrom(null);

        return check;
    }

    /**
     * Метод предназначен для проведения операции по снятию денежных средств со счета
     * и работает над операциями типа TransactionType.WITHDRAWALS
     * @param transaction объект, содержащий детали проводимой операции,
     *                    в частности: - счет, с которого будут сняты денежные средства,
     *                                 - сумма к списанию,
     *                                 - тип проводимой транзакции,
     *                                 - валюта операции
     * @return объект Check, содержащий информацию о проведенной операции
     */
    @Override
    public Check withdrawalMoney(Transaction transaction) {
        if (!transaction.getType().equals(TransactionType.WITHDRAWALS)){
            throw new RuntimeException("Transaction type doesn't match the operation being performed");//TODO custom exception
        }

        double sum = transaction.getSum();
        checkTransactionSum(sum);

        UUID accountFrom = transaction.getAccountFrom();
        AccountEntity accountEntity = accountRepository.checkAccountExistence(accountFrom);

        Currency currency = transaction.getCurrency();
        checkAccountCurrency(accountEntity, currency);

        checkAccountBalance(accountEntity, sum);

        transaction.setSum(-sum);

        transaction.setId(UUID.randomUUID());

        Check check = convertTransactionToCheck(accountRepository.updateBalanceCashOperation(transaction));

        publisher.notify(new CheckEvent(check));

        check.setBankTo(null);

        return check;
    }

    /**
     * Метод предназначен для проведения операций между банковскими счетами, открытыми как в одном банке, так и в различных.
     * Поддерживаемые типы транзакций: TransactionType.WAGE, TransactionType.MONEY_TRANSFER, TransactionType.PAYMENT_FOR_SERVICES
     * @param transaction объект, содержащий детали проводимой операции,
     *                    в частности: - счета, между которыми будет осуществлено списание/зачисление денежных средств,
     *                                 - сумма операции,
     *                                 - тип проводимой транзакции,
     *                                 - валюта операции
     */
    @Override
    public Check transferMoney(Transaction transaction) {
        TransactionType type = transaction.getType();
        if (!type.equals(TransactionType.WAGE) && !type.equals(TransactionType.MONEY_TRANSFER) && !type.equals(TransactionType.PAYMENT_FOR_SERVICES)){
            throw new RuntimeException("Transaction type doesn't match the operation being performed");//TODO custom exception
        }

        double sum = transaction.getSum();
        checkTransactionSum(sum);

        Currency currency = transaction.getCurrency();
        UUID accountFrom = transaction.getAccountFrom();
        UUID accountTo = transaction.getAccountTo();

        AccountEntity accountEntityFrom = accountRepository.checkAccountExistence(accountFrom);
        checkAccountCurrency(accountEntityFrom, currency);
        checkAccountBalance(accountEntityFrom, sum);

        AccountEntity accountEntityTo = accountRepository.checkAccountExistence(accountTo);
        checkAccountCurrency(accountEntityTo, currency);

        transaction.setId(UUID.randomUUID());

        Check check = convertTransactionToCheck(accountRepository.updateBalanceCashlessPayments(transaction));

        publisher.notify(new CheckEvent(check));

        return check;
    }

    /**
     * Метод проверяет необходимость начисления ежемесячных процентов.
     * Пороговое время для начисления 23:59:30.
     */
    @Override
    public void checkTheNeedToCalculateInterest(LocalDateTime localDateTime) {
        LocalTime localTime = localDateTime.toLocalTime();
        LocalDate localDate = localDateTime.toLocalDate();

        LocalTime expected = LocalTime.of(23, 59, 30);

        int lengthOfMonth =localDate.lengthOfMonth();
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

    /**
     * Метод обеспечивает начисление ежемесячных процентов
     */
    @Override
    public void calculateInterest() {
        accountRepository.calculateMonthlyInterest();
    }


    /**Метод предоставляет полную информацию по открытому счету
     * @param account счет, по которому запрашивается информация
     * @return - Account, содержит всю информацию по счету,
     */
    @Override
    public Account getAccountInfo(UUID account) {
        AccountEntity entity = accountRepository.getAccountInfo(account);
        return convertFromEntity(entity);
    }

    /**
     * Метод проверяет валидность суммы операции (сумма должна быть положительным числом)
     * @param sum - сумма транзакции
     */
    private void checkTransactionSum(double sum) {
        if(sum <= 0) {
            throw new RuntimeException("The amount of any transaction must be greater than zero");
        }
    }

    /** Метод проверяет, совпадает ли валюта платежа и валюта счета
     * @param entity аккаунт, по которому будет совершен платеж
     * @param currency валюта платежа
     * В случае, если валюта счета и валюта платежа не совпадают, выбрасывается RuntimeException
     */
    private void checkAccountCurrency(AccountEntity entity, Currency currency) {
        if (!entity.getCurrency().equals(currency.name())){
            throw new RuntimeException("Payment currency doesn't match with account currency");//TODO custom exception
        }
    }

    /** Метод проверяет, достаточен ли баланс для совершения заданного платежа
     * @param accountEntity аккаунт, по которому будет совершен платеж
     * @param sum сумма платежа
     * В случае, если баланс счета недостаточен, выбрасывается RuntimeException
     */
    private void checkAccountBalance(AccountEntity accountEntity, double sum) {
        if(accountEntity.getBalance() < sum) {
            throw new RuntimeException("You don't have enough money for this operation");//TODO custom exception
        }
    }

    /**
     * Метод конвертирует объект Transaction в объект Check
     * @param transaction - объект-источник
     * @return Check
     */
    private Check convertTransactionToCheck(Transaction transaction) {
        UUID accountFrom = transaction.getAccountFrom();
        UUID accountTo = transaction.getAccountTo();

        Bank bankFrom = getBank(accountFrom);
        Bank bankTo = getBank(accountTo);

        return Check.builder()
                .setNumber(transaction.getId())
                .setLocalDateTime(transaction.getDate())
                .setTransactionType(transaction.getType())
                .setBankFrom(bankFrom)
                .setBankTo(bankTo)
                .setAccountFrom(accountFrom)
                .setAccountTo(accountTo)
                .setSum(Math.abs(transaction.getSum()))
                .setCurrency(transaction.getCurrency())
                .build();
    }

    /**
     * Метод конвертирует объект AccountEntity в объект Account
     * @param entity - объект-источник
     * @return Account
     */
    private Account convertFromEntity(AccountEntity entity) {
        return Account.builder()
                .setNum(entity.getNum())
                .setCurrency(Currency.valueOf(entity.getCurrency()))
                .setBank(new Bank(entity.getBank().getId(), entity.getBank().getName()))
                .setDateOpen(entity.getDateOpen())
                .setDateLastTransaction(entity.getDateLastTransaction())
                .setBalance(entity.getBalance())
                .setOwner(new Client(entity.getOwner().getId(), entity.getOwner().getName()))
                .build();
    }

    /**
     * * Метод предоставляет информацию о банке, в котором открыт счет.
     * @param account счет. Если поле == null, то возвращается объект-заглушка.
     * @return Bank, содержит информацию о банке, в котором открыт счет
     */
    private Bank getBank(UUID account) {
        return account == null ? new Bank("") : bankService.getBankByAccount(account);
    }
}
