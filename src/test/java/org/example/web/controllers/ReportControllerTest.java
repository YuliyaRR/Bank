package org.example.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.core.dto.*;
import org.example.core.dto.docs.AccountStatement;
import org.example.core.dto.docs.MoneyStatement;
import org.example.service.impl.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {
    @InjectMocks
    private ReportController reportController;
    @Mock
    private ReportService reportService;
    @Spy
    private ObjectMapper mapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter writer;
    @Captor
    private ArgumentCaptor<String> captorString;
    private Account account;
    private final static String BANK_NAME = "Clever-Bank";
    private final static String PARAMETER_REPORT_DURATION = "duration";
    private final static String PARAMETER_REPORT_ACCOUNT = "account";
    private final static String PARAMETER_REPORT_TYPE = "report_type";
    private final static String PARAMETER_REPORT_DATE_FROM = "from";
    private final static String PARAMETER_REPORT_DATE_TO = "to";
    private final static String PARAMETER_REPORT_DATE_START = "start";
    private final static String EXCEPTION_INVALID_UUID = "UUID not valid";
    private final static String EXCEPTION_INVALID_DATA = "Data not entered";
    private final static String EXCEPTION_INVALID_DATE_FORMAT = "Invalid date format";
    private static final String ACCOUNT_UUID = "12605bea-4688-4a8a-b64f-d29e24eb6d81";

    @BeforeEach
    public void setUp() {
        mapper.registerModule(new JavaTimeModule());
        this.account = Account.builder()
                .setNum(UUID.fromString(ACCOUNT_UUID))
                .setCurrency(Currency.RUB)
                .setBank(new Bank(BANK_NAME))
                .setDateOpen(LocalDate.of(2022, 10, 15))
                .setDateLastTransaction(LocalDateTime.of(2023, 10, 16, 14, 55))
                .setBalance(150.26)
                .setOwner(new Client(UUID.randomUUID(), "Test Client 1"))
                .build();
    }

    @Test
    public void doGetAccountStatementWhenDataIsValid() throws IOException, ServletException {
        AccountStatement expectedAS = AccountStatement.builder()
                .setAccount(account)
                .setPeriod(new Period(LocalDate.of(2022, 10, 15), LocalDate.of(2023, 9, 10)))
                .setTransaction(Transaction.builder()
                        .setId(UUID.randomUUID())
                        .setCurrency(Currency.RUB)
                        .setDate(LocalDateTime.of(2023, 10, 16, 14, 55))
                        .setAccountFrom(UUID.fromString(ACCOUNT_UUID))
                        .setAccountTo(UUID.randomUUID())
                        .setSum(100)
                        .setType(TransactionType.MONEY_TRANSFER)
                        .build())
                .build();

        String expJson = mapper.writeValueAsString(expectedAS);

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_REPORT_ACCOUNT)).thenReturn(ACCOUNT_UUID);
        when(request.getParameter(PARAMETER_REPORT_TYPE)).thenReturn(ReportType.ACCOUNT_STATEMENT.name());
        when(request.getParameter(PARAMETER_REPORT_DURATION)).thenReturn(Duration.ALL.name());
        when(request.getParameter(PARAMETER_REPORT_DATE_START)).thenReturn("2023-09-10");
        when(reportService.getAccountStatement(any(UUID.class), any(Duration.class), any(LocalDate.class))).thenReturn(expectedAS);

        reportController.doGet(request, response);

        verify(writer).write(captorString.capture());

        String respJson = captorString.getValue();

        assertEquals(expJson, respJson);

        verify(reportService, times(1)).getAccountStatement(any(UUID.class), any(Duration.class), any(LocalDate.class));
        verify(writer, times(1)).write(any(String.class));
    }

    @Test
    public void doGetWhenAccountUUIDNotValidThenPrintExceptionMessage() throws IOException, ServletException {
        String uuidInvalid = "12605bea-4688-a8a-b64f-d29e24eb6d81";

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_REPORT_ACCOUNT)).thenReturn(uuidInvalid);

        reportController.doGet(request, response);

        verify(writer).write(captorString.capture());

        String message = captorString.getValue();

        assertEquals(EXCEPTION_INVALID_UUID, message);

        verify(reportService, times(0)).getAccountStatement(any(UUID.class), any(Duration.class), any(LocalDate.class));
        verify(writer, times(1)).write(any(String.class));
    }

    @Test
    public void doGetWhenReportTypeNotValidThenPrintExceptionMessage() throws IOException, ServletException {
        String reportTypeInvalid = "";

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_REPORT_ACCOUNT)).thenReturn(ACCOUNT_UUID);
        when(request.getParameter(PARAMETER_REPORT_TYPE)).thenReturn(reportTypeInvalid);

        reportController.doGet(request, response);

        verify(writer).write(captorString.capture());

        String message = captorString.getValue();

        assertEquals(EXCEPTION_INVALID_DATA, message);

        verify(reportService, times(0)).getAccountStatement(any(UUID.class), any(Duration.class), any(LocalDate.class));
        verify(writer, times(1)).write(any(String.class));
    }

    @Test
    public void doGetAccountStatementWhenReportDurationNotValidThenPrintExceptionMessage() throws IOException, ServletException {
        String reportDurationInvalid = " ";

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_REPORT_ACCOUNT)).thenReturn(ACCOUNT_UUID);
        when(request.getParameter(PARAMETER_REPORT_TYPE)).thenReturn(ReportType.ACCOUNT_STATEMENT.name());
        when(request.getParameter(PARAMETER_REPORT_DURATION)).thenReturn(reportDurationInvalid);

        reportController.doGet(request, response);

        verify(writer).write(captorString.capture());

        String message = captorString.getValue();

        assertEquals(EXCEPTION_INVALID_DATA, message);

        verify(reportService, times(0)).getAccountStatement(any(UUID.class), any(Duration.class), any(LocalDate.class));
        verify(writer, times(1)).write(any(String.class));
    }

    @Test
    public void doGetAccountStatementWhenReportDateStartNotEnteredThenPrintExceptionMessage() throws IOException, ServletException {
        String reportDateStartInvalid = "";

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_REPORT_ACCOUNT)).thenReturn(ACCOUNT_UUID);
        when(request.getParameter(PARAMETER_REPORT_TYPE)).thenReturn(ReportType.ACCOUNT_STATEMENT.name());
        when(request.getParameter(PARAMETER_REPORT_DURATION)).thenReturn(Duration.ALL.name());
        when(request.getParameter(PARAMETER_REPORT_DATE_START)).thenReturn(reportDateStartInvalid);

        reportController.doGet(request, response);

        verify(writer).write(captorString.capture());

        String message = captorString.getValue();

        assertEquals(EXCEPTION_INVALID_DATA, message);

        verify(reportService, times(0)).getAccountStatement(any(UUID.class), any(Duration.class), any(LocalDate.class));
        verify(writer, times(1)).write(any(String.class));
    }

    @Test
    public void doGetAccountStatementWhenReportDateStartNotValidThenPrintExceptionMessage() throws IOException, ServletException {
        String reportDateStartInvalid = "hsdjfsx";

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_REPORT_ACCOUNT)).thenReturn(ACCOUNT_UUID);
        when(request.getParameter(PARAMETER_REPORT_TYPE)).thenReturn(ReportType.ACCOUNT_STATEMENT.name());
        when(request.getParameter(PARAMETER_REPORT_DURATION)).thenReturn(Duration.ALL.name());
        when(request.getParameter(PARAMETER_REPORT_DATE_START)).thenReturn(reportDateStartInvalid);

        reportController.doGet(request, response);

        verify(writer).write(captorString.capture());

        String message = captorString.getValue();

        assertEquals(EXCEPTION_INVALID_DATE_FORMAT, message);

        verify(reportService, times(0)).getAccountStatement(any(UUID.class), any(Duration.class), any(LocalDate.class));
        verify(writer, times(1)).write(any(String.class));
    }

    @Test
    public void doGetMoneyStatementWhenDataIsValid() throws IOException, ServletException {
        Period period = new Period(LocalDate.of(2023, 9, 1), LocalDate.of(2023, 9, 10));
        SumTransactionsInfo sumInfo = new SumTransactionsInfo(1500, 350);

        MoneyStatement expectedMS = MoneyStatement.builder()
                .setAccount(account)
                .setPeriod(period)
                .setSumTransactionsInfo(sumInfo)
                .build();

        String expJson = mapper.writeValueAsString(expectedMS);

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_REPORT_ACCOUNT)).thenReturn(ACCOUNT_UUID);
        when(request.getParameter(PARAMETER_REPORT_TYPE)).thenReturn(ReportType.MONEY_STATEMENT.name());
        when(request.getParameter(PARAMETER_REPORT_DATE_FROM)).thenReturn("2023-09-01");
        when(request.getParameter(PARAMETER_REPORT_DATE_TO)).thenReturn("2023-09-10");
        when(reportService.getMoneyStatement(any(UUID.class), any(Period.class))).thenReturn(expectedMS);

        reportController.doGet(request, response);

        verify(writer).write(captorString.capture());

        String respJson = captorString.getValue();

        assertEquals(expJson, respJson);

        verify(reportService, times(1)).getMoneyStatement(any(UUID.class), any(Period.class));
        verify(writer, times(1)).write(any(String.class));
    }

    @Test
    public void doGetMoneyStatementWhenReportDateFromNotEnteredThenPrintExceptionMessage() throws IOException, ServletException {
        String reportDateFromInvalid = null;

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_REPORT_ACCOUNT)).thenReturn(ACCOUNT_UUID);
        when(request.getParameter(PARAMETER_REPORT_TYPE)).thenReturn(ReportType.MONEY_STATEMENT.name());
        when(request.getParameter(PARAMETER_REPORT_DATE_FROM)).thenReturn(reportDateFromInvalid);

        reportController.doGet(request, response);

        verify(writer).write(captorString.capture());

        String message = captorString.getValue();

        assertEquals(EXCEPTION_INVALID_DATA, message);

        verify(reportService, times(0)).getMoneyStatement(any(UUID.class), any(Period.class));
        verify(writer, times(1)).write(any(String.class));
    }

    @Test
    public void doGetMoneyStatementWhenReportDateFromNotValidThenPrintExceptionMessage() throws IOException, ServletException {
        String reportDateFromInvalid = "jsvhnlsvmxvn";

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_REPORT_ACCOUNT)).thenReturn(ACCOUNT_UUID);
        when(request.getParameter(PARAMETER_REPORT_TYPE)).thenReturn(ReportType.MONEY_STATEMENT.name());
        when(request.getParameter(PARAMETER_REPORT_DATE_FROM)).thenReturn(reportDateFromInvalid);

        reportController.doGet(request, response);

        verify(writer).write(captorString.capture());

        String message = captorString.getValue();

        assertEquals(EXCEPTION_INVALID_DATE_FORMAT, message);

        verify(reportService, times(0)).getMoneyStatement(any(UUID.class), any(Period.class));
        verify(writer, times(1)).write(any(String.class));
    }

    @Test
    public void doGetMoneyStatementWhenReportDateToNotEnteredThenPrintExceptionMessage() throws IOException, ServletException {
        String reportDateToInvalid = "";

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_REPORT_ACCOUNT)).thenReturn(ACCOUNT_UUID);
        when(request.getParameter(PARAMETER_REPORT_TYPE)).thenReturn(ReportType.MONEY_STATEMENT.name());
        when(request.getParameter(PARAMETER_REPORT_DATE_FROM)).thenReturn("2023-09-01");
        when(request.getParameter(PARAMETER_REPORT_DATE_TO)).thenReturn(reportDateToInvalid);

        reportController.doGet(request, response);

        verify(writer).write(captorString.capture());

        String message = captorString.getValue();

        assertEquals(EXCEPTION_INVALID_DATA, message);

        verify(reportService, times(0)).getMoneyStatement(any(UUID.class), any(Period.class));
        verify(writer, times(1)).write(any(String.class));
    }

    @Test
    public void doGetMoneyStatementWhenReportDateToNotValidThenPrintExceptionMessage() throws IOException, ServletException {
        String reportDateToInvalid = "zsxdfgkjhgfdg";

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_REPORT_ACCOUNT)).thenReturn(ACCOUNT_UUID);
        when(request.getParameter(PARAMETER_REPORT_TYPE)).thenReturn(ReportType.MONEY_STATEMENT.name());
        when(request.getParameter(PARAMETER_REPORT_DATE_FROM)).thenReturn("2023-09-01");
        when(request.getParameter(PARAMETER_REPORT_DATE_TO)).thenReturn(reportDateToInvalid);

        reportController.doGet(request, response);

        verify(writer).write(captorString.capture());

        String message = captorString.getValue();

        assertEquals(EXCEPTION_INVALID_DATE_FORMAT, message);

        verify(reportService, times(0)).getMoneyStatement(any(UUID.class), any(Period.class));
        verify(writer, times(1)).write(any(String.class));
    }
}