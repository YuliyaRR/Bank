package org.example.service.impl;

import org.example.core.dto.*;
import org.example.core.dto.docs.AccountStatement;
import org.example.core.dto.docs.MoneyStatement;
import org.example.core.events.AccountStatementEvent;
import org.example.core.events.MoneyStatementEvent;
import org.example.listener.impl.accountStatement.AccountStatementPublisher;
import org.example.listener.impl.moneyStatement.MoneyStatementPublisher;
import org.example.service.api.IAccountService;
import org.example.service.api.IReportService;
import org.example.service.api.ITransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
    private final static UUID NUM_ACC = UUID.fromString("12605bea-4688-4a8a-b64f-d29e24eb6d81");
    private final static String BANK_NAME = "Clever-Bank";
    private final static String PROPERTY_NAME = "BANK_NAME";
    private final static String EXCEPTION_INVALID_DATE = "The date can't be later than the current date";
    private final static String EXCEPTION_INVALID_BANK = "Your account doesn't belong to CleverBank. We can't create reports for you.";
    private final static String EXCEPTION_INVALID_PERIOD = "Invalid time period";
    private final static String EXCEPTION_DATE_OPEN_AFTER_DATE_FROM = "The start date of searching for cash flow information cannot be earlier than the account opening date";
    @Mock
    private IAccountService accountService;
    @Mock
    private ITransactionService transactionService;
    @Mock
    private AccountStatementPublisher publisherAS;
    @Mock
    private MoneyStatementPublisher publisherMS;
    @Mock
    private Properties properties;
    private IReportService reportService;
    private Account account;

    @BeforeEach
    public void setUp() {
        this.reportService = new ReportService(accountService, transactionService, publisherAS, publisherMS, properties);
        this.account = Account.builder()
                .setNum(NUM_ACC)
                .setCurrency(Currency.RUB)
                .setBank(new Bank(BANK_NAME))
                .setDateOpen(LocalDate.of(2022, 10, 15))
                .setDateLastTransaction(LocalDateTime.of(2023, 10, 16, 14, 55))
                .setBalance(150.26)
                .setOwner(new Client(UUID.randomUUID(), "Test Client 1"))
                .build();

    }

    @Test
    public void getAccountStatementWhenDataValid() {
        Duration duration = Duration.MONTH;
        LocalDate dateStart = LocalDate.of(2023, 10, 20);

        Transaction transaction = Transaction.builder()
                .setId(UUID.randomUUID())
                .setCurrency(Currency.BYN)
                .setDate(LocalDateTime.of(2023, 10, 19, 14, 58))
                .setAccountFrom(UUID.randomUUID())
                .setAccountTo(UUID.randomUUID())
                .setSum(100)
                .setType(TransactionType.MONEY_TRANSFER)
                .build();

        Transaction transaction2 = Transaction.builder()
                .setId(UUID.randomUUID())
                .setCurrency(Currency.BYN)
                .setDate(LocalDateTime.of(2023, 10, 5, 14, 58))
                .setAccountFrom(UUID.randomUUID())
                .setAccountTo(UUID.randomUUID())
                .setSum(-100)
                .setType(TransactionType.WAGE)
                .build();

        Transaction transaction3 = Transaction.builder()
                .setId(UUID.randomUUID())
                .setCurrency(Currency.BYN)
                .setDate(LocalDateTime.of(2023, 9, 28, 14, 58))
                .setAccountFrom(UUID.randomUUID())
                .setAccountTo(UUID.randomUUID())
                .setSum(1300)
                .setType(TransactionType.PAYMENT_FOR_SERVICES)
                .build();

        List<Transaction> listTransactions = new ArrayList<>(List.of(transaction, transaction2, transaction3));

        when(accountService.getAccountInfo(NUM_ACC)).thenReturn(account);
        when(properties.getProperty(PROPERTY_NAME)).thenReturn(BANK_NAME);
        when(transactionService.allAccountTransactions(any(), any(Period.class))).thenReturn(listTransactions);

        AccountStatement expected = AccountStatement.builder()
                .setAccount(account)
                .setTransactions(listTransactions)
                .build();

        AccountStatement accountStatement = reportService.getAccountStatement(NUM_ACC, duration, dateStart);

        assertNotNull(accountStatement);
        assertEquals(expected.getAccount(), (accountStatement.getAccount()));
        assertEquals(expected.getTransactions(), accountStatement.getTransactions());

        verify(accountService, times(1)).getAccountInfo(NUM_ACC);
        verify(properties, times(1)).getProperty(PROPERTY_NAME);
        verify(transactionService, times(1)).allAccountTransactions(any(), any(Period.class));
        verify(publisherAS, times(1)).notify(any(AccountStatementEvent.class));
    }

    @Test
    public void getAccountStatementWhenStartDateNotValidThenThrowException() {
        Duration duration = Duration.MONTH;
        LocalDate dateStart = LocalDate.of(2025, 10, 20);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reportService.getAccountStatement(NUM_ACC, duration, dateStart));
        assertEquals(EXCEPTION_INVALID_DATE, exception.getMessage());
    }

    @Test
    public void getAccountStatementWhenBankNotValidThenThrowException() {
        this.account.setBank(new Bank("TestBank"));
        Duration duration = Duration.MONTH;
        LocalDate dateStart = LocalDate.of(2023, 10, 20);

        when(accountService.getAccountInfo(NUM_ACC)).thenReturn(account);
        when(properties.getProperty(PROPERTY_NAME)).thenReturn(BANK_NAME);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reportService.getAccountStatement(NUM_ACC, duration, dateStart));
        assertEquals(EXCEPTION_INVALID_BANK, exception.getMessage());
    }

    @Test
    public void getAccountStatementCheckPeriodWhenDurationIsMonthThenDateFromIsMonthBefore() {
        Duration duration = Duration.MONTH;
        LocalDate dateStart = LocalDate.of(2023, 10, 20);
        List<Transaction> listTransactions = new ArrayList<>();

        when(accountService.getAccountInfo(NUM_ACC)).thenReturn(account);
        when(properties.getProperty(PROPERTY_NAME)).thenReturn(BANK_NAME);
        when(transactionService.allAccountTransactions(any(), any(Period.class))).thenReturn(listTransactions);

        AccountStatement accountStatement = reportService.getAccountStatement(NUM_ACC, duration, dateStart);

        Period expected = new Period(LocalDate.of(2023, 9, 20), dateStart);

        assertNotNull(accountStatement);
        assertEquals(expected, accountStatement.getPeriod());

        verify(accountService, times(1)).getAccountInfo(NUM_ACC);
        verify(properties, times(1)).getProperty(PROPERTY_NAME);
        verify(transactionService, times(1)).allAccountTransactions(any(), any(Period.class));
        verify(publisherAS, times(1)).notify(any(AccountStatementEvent.class));
    }

    @Test
    public void getAccountStatementCheckPeriodWhenDurationIsYearThenDateFromIsYearBefore() {
        Duration duration = Duration.YEAR;
        LocalDate dateStart = LocalDate.of(2023, 10, 20);
        List<Transaction> listTransactions = new ArrayList<>();

        when(accountService.getAccountInfo(NUM_ACC)).thenReturn(account);
        when(properties.getProperty(PROPERTY_NAME)).thenReturn(BANK_NAME);
        when(transactionService.allAccountTransactions(any(), any(Period.class))).thenReturn(listTransactions);

        AccountStatement accountStatement = reportService.getAccountStatement(NUM_ACC, duration, dateStart);

        Period expected = new Period(LocalDate.of(2022, 10, 20), dateStart);

        assertNotNull(accountStatement);
        assertEquals(expected, accountStatement.getPeriod());

        verify(accountService, times(1)).getAccountInfo(NUM_ACC);
        verify(properties, times(1)).getProperty(PROPERTY_NAME);
        verify(transactionService, times(1)).allAccountTransactions(any(), any(Period.class));
        verify(publisherAS, times(1)).notify(any(AccountStatementEvent.class));
    }

    @Test
    public void getAccountStatementCheckPeriodWhenDurationIsAllThenDateFromIsDateOpenAccount() {
        Duration duration = Duration.ALL;
        LocalDate dateStart = LocalDate.of(2023, 10, 20);
        List<Transaction> listTransactions = new ArrayList<>();

        when(accountService.getAccountInfo(NUM_ACC)).thenReturn(account);
        when(properties.getProperty(PROPERTY_NAME)).thenReturn(BANK_NAME);
        when(transactionService.allAccountTransactions(any(), any(Period.class))).thenReturn(listTransactions);

        AccountStatement accountStatement = reportService.getAccountStatement(NUM_ACC, duration, dateStart);

        Period expected = new Period(account.getDateOpen(), dateStart);

        assertNotNull(accountStatement);
        assertEquals(expected, accountStatement.getPeriod());

        verify(accountService, times(1)).getAccountInfo(NUM_ACC);
        verify(properties, times(1)).getProperty(PROPERTY_NAME);
        verify(transactionService, times(1)).allAccountTransactions(any(), any(Period.class));
        verify(publisherAS, times(1)).notify(any(AccountStatementEvent.class));
    }

    @Test
    public void getAccountStatementCheckPeriodWhenDurationLongerThanPeriodExistenceAccountThenDateFromIsDateOpenAccount() {
        Duration duration = Duration.YEAR;
        LocalDate dateStart = LocalDate.of(2023, 1, 20);
        List<Transaction> listTransactions = new ArrayList<>();

        when(accountService.getAccountInfo(NUM_ACC)).thenReturn(account);
        when(properties.getProperty(PROPERTY_NAME)).thenReturn(BANK_NAME);
        when(transactionService.allAccountTransactions(any(), any(Period.class))).thenReturn(listTransactions);

        AccountStatement accountStatement = reportService.getAccountStatement(NUM_ACC, duration, dateStart);

        Period expected = new Period(account.getDateOpen(), dateStart);

        assertNotNull(accountStatement);
        assertEquals(expected, accountStatement.getPeriod());

        verify(accountService, times(1)).getAccountInfo(NUM_ACC);
        verify(properties, times(1)).getProperty(PROPERTY_NAME);
        verify(transactionService, times(1)).allAccountTransactions(any(), any(Period.class));
        verify(publisherAS, times(1)).notify(any(AccountStatementEvent.class));
    }

    @Test
    public void getMoneyStatementWhenAllDataIsValid() {
        Period period = new Period(LocalDate.of(2023, 9, 10), LocalDate.of(2023, 9, 25));
        SumTransactionsInfo sumInfo = new SumTransactionsInfo(1500, 350);

        when(accountService.getAccountInfo(NUM_ACC)).thenReturn(account);
        when(properties.getProperty(PROPERTY_NAME)).thenReturn(BANK_NAME);
        when(transactionService.getSumInfoAboutTransactions(NUM_ACC, period)).thenReturn(sumInfo);

        MoneyStatement expected = MoneyStatement.builder()
                .setAccount(account)
                .setPeriod(period)
                .setSumTransactionsInfo(sumInfo)
                .build();

        MoneyStatement moneyStatement = reportService.getMoneyStatement(NUM_ACC, period);

        assertNotNull(moneyStatement);
        assertEquals(expected.getAccount(), moneyStatement.getAccount());
        assertEquals(expected.getPeriod(), moneyStatement.getPeriod());
        assertEquals(expected.getSumTransactionsInfo(), moneyStatement.getSumTransactionsInfo());

        verify(accountService, times(1)).getAccountInfo(NUM_ACC);
        verify(properties, times(1)).getProperty(PROPERTY_NAME);
        verify(transactionService, times(1)).getSumInfoAboutTransactions(any(), any(Period.class));
        verify(publisherMS, times(1)).notify(any(MoneyStatementEvent.class));
    }

    @Test
    public void getMoneyStatementWhenDateFromInPeriodNotValidThenThrowException() {
        Period period = new Period(LocalDate.of(2100, 9, 10), LocalDate.of(2023, 9, 25));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reportService.getMoneyStatement(NUM_ACC, period));
        assertEquals(EXCEPTION_INVALID_DATE, exception.getMessage());
    }

    @Test
    public void getMoneyStatementWhenDateToInPeriodNotValidThenThrowException() {
        Period period = new Period(LocalDate.of(2023, 9, 10), LocalDate.of(2100, 9, 25));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reportService.getMoneyStatement(NUM_ACC, period));
        assertEquals(EXCEPTION_INVALID_DATE, exception.getMessage());
    }

    @Test
    public void getMoneyStatementWhenDateFromAfterDateToInPeriodThenThrowException() {
        Period period = new Period(LocalDate.of(2023, 9, 10), LocalDate.of(2023, 2, 25));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reportService.getMoneyStatement(NUM_ACC, period));
        assertEquals(EXCEPTION_INVALID_PERIOD, exception.getMessage());
    }

    @Test
    public void getMoneyStatementWhenDateFromEarlierThanAccountOpeningDateThenThrowException() {
        Period period = new Period(LocalDate.of(2020, 9, 10), LocalDate.of(2023, 2, 25));

        when(accountService.getAccountInfo(NUM_ACC)).thenReturn(account);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reportService.getMoneyStatement(NUM_ACC, period));
        assertEquals(EXCEPTION_DATE_OPEN_AFTER_DATE_FROM, exception.getMessage());
    }

    @Test
    public void getMoneyStatementWhenBankNotValidThenThrowException() {
        this.account.setBank(new Bank("TestBank"));
        Period period = new Period(LocalDate.of(2023, 9, 10), LocalDate.of(2023, 9, 25));

        when(accountService.getAccountInfo(NUM_ACC)).thenReturn(account);
        when(properties.getProperty(PROPERTY_NAME)).thenReturn(BANK_NAME);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> reportService.getMoneyStatement(NUM_ACC, period));
        assertEquals(EXCEPTION_INVALID_BANK, exception.getMessage());
    }






}
