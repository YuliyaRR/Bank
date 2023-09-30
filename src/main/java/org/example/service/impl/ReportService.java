package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.dto.*;
import org.example.core.dto.docs.AccountStatement;
import org.example.core.dto.docs.MoneyStatement;
import org.example.core.events.AccountStatementEvent;
import org.example.core.events.MoneyStatementEvent;
import org.example.listener.api.IPublisher;
import org.example.service.api.IAccountService;
import org.example.service.api.IReportService;
import org.example.service.api.ITransactionService;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@RequiredArgsConstructor
public class ReportService implements IReportService {
    private final IAccountService accountService;
    private final ITransactionService transactionService;
    private final IPublisher<AccountStatementEvent> publisherAS;
    private final IPublisher<MoneyStatementEvent> publisherMS;
    private final Properties properties;
    private final static String BANK_NAME = "BANK_NAME";

    /**
     * Метод формирует выписку по счету за заданный период времени. Выписки формируются только для клиентов Clever-Bank
     * @param account счет, по которому будет сформирована выписка по счету
     * @param duration продолжительность, за которую нужно составить документ.
     * Доступные варианты: месяц, год, весь период обслуживания.
     * @return выписка по счету клиента
     */
    @Override
    public AccountStatement getAccountStatement(UUID account, Duration duration) {
        Account accountInfo = accountService.getAccountInfo(account);
        LocalDate dateOpen = accountInfo.getDateOpen();

        checkBank(accountInfo.getBank());

        LocalDate dateFrom = getSearchStartDay(duration, dateOpen);
        LocalDate dateTo = LocalDate.now();

        Period period = new Period(dateFrom, dateTo);

        List<Transaction> transactionList = transactionService.allAccountTransactions(account, period);

        AccountStatement accountStatement = AccountStatement.builder()
                .setAccount(accountInfo)
                .setPeriod(period)
                .setCreationTime(LocalDateTime.now())
                .setTransactions(transactionList)
                .build();

        publisherAS.notify(new AccountStatementEvent(accountStatement));

        return accountStatement;

    }

    @Override
    public MoneyStatement getMoneyStatement(UUID account, Period period) {
        checkDate(period);

        Account accountInfo = accountService.getAccountInfo(account);

        if(period.getDateFrom().isBefore(accountInfo.getDateOpen())){
            throw new RuntimeException("The start date of searching for cash flow information cannot be earlier than the account opening date");
        }

        checkBank(accountInfo.getBank());

        SumTransactionsInfo sumInfoAboutTransactions = transactionService.getSumInfoAboutTransactions(account, period);

        MoneyStatement moneyStatement = MoneyStatement.builder()
                .setAccount(accountInfo)
                .setPeriod(period)
                .setCreationTime(LocalDateTime.now())
                .setSumTransactionsInfo(sumInfoAboutTransactions)
                .build();

        publisherMS.notify(new MoneyStatementEvent(moneyStatement));

        return moneyStatement;
    }

    /** Метод проверяет, является ли банк клиента - Clever-Bank'ом
     * @param bank банк клиента
     * В случае, елси переданный банк - не Clever-Bank, выбрасывается RuntimeException
     */
    private void checkBank(Bank bank) {
        if (!bank.getName().equals(properties.getProperty(BANK_NAME))) {
            throw new RuntimeException("Your account doesn't belong to CleverBank. We can't create reports for you.");//TODO custom exception
        }
    }

    /** Метод устанавливает даты начала и конца выборки транзакций
     * @param duration выбранная продолжительность
     * @param dateOpen дата открытия счета
     * @return дата начала выборки
     */
    private LocalDate getSearchStartDay(Duration duration, LocalDate dateOpen) {
        LocalDate now = LocalDate.now();
        LocalDate from;

        if (duration == Duration.MONTH) {
            from = now.minusMonths(1);
        } else if (duration == Duration.YEAR) {
            from = now.minusYears(1);
        } else {
            from = dateOpen;
        }

        if(from.isBefore(dateOpen)) {
            from = dateOpen;
        }

        return from;
    }

    private void checkDate(Period period) {
        LocalDate now = LocalDate.now();
        LocalDate from = period.getDateFrom();
        LocalDate to = period.getDateTo();

        if (from.isAfter(now) || to.isAfter(now)) {
            throw new RuntimeException("The date can't be later than the current date");
        }

        if(from.isAfter(to)) {
            throw new RuntimeException("Invalid time period");
        }
    }

}
