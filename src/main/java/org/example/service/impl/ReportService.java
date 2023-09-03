package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.dto.*;
import org.example.core.events.AccountStatementEvent;
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
    private final IPublisher<AccountStatementEvent> publisher;
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

        publisher.notify(new AccountStatementEvent(accountStatement));

        return accountStatement;

    }

    /** Метод проверяет, является ли банк клиента - Clever-Bank'ом
     * @param bank банк клиента
     * В случае, елси переданный банк - не Clever-Bank, выбрасывается RuntimeException
     */
    private void checkBank(Bank bank) {
        if (!bank.getName().equals(properties.getProperty(BANK_NAME))) {
            throw new RuntimeException("Your account doesn't belong to CleverBank. We can't create account statement for you.");//TODO custom exception
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

}
