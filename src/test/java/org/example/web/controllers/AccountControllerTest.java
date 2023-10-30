package org.example.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.dto.Currency;
import org.example.core.dto.Transaction;
import org.example.core.dto.TransactionType;
import org.example.core.dto.docs.Check;
import org.example.service.api.IAccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
    @InjectMocks
    private AccountController accountController;
    @Mock
    private IAccountService accountService;
    @Spy
    private ObjectMapper mapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter writer;
    @Captor
    private ArgumentCaptor<Transaction> captorTransaction;
    @Captor
    private ArgumentCaptor<String> captorString;
    private static final String UUID_ACCOUNT_FROM = "8e605bea-4688-4a8a-b64f-d29e24eb6d81";
    private static final String UUID_ACCOUNT_TO = "a7165b1d-439d-4210-b576-48590b922f1e";
    private static final String EXCEPTION_INVALID_SUM = "The amount of any transaction must be greater than zero";
    private static final String EXCEPTION_INVALID_OPERATION = "The requested operations aren't currently supported";
    private static final String EXCEPTION_ACCOUNT_NOT_ENTERED = "Account not entered";
    private static final String EXCEPTION_ACCOUNT_NOT_FOUND = "Account not found";
    @Test
    public void doPostWithdrawalMoneyWhenDataIsValidThenPrintCheck() throws IOException, ServletException {
        String json = String.format("{\"currency\" : \"EUR\",\"account_from\" : \"%s\",\"sum\" : 1000.40, \"type\" : \"Cash withdrawal\"}", UUID_ACCOUNT_FROM);
        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        Check check = Check.builder()
                .setTransactionType(TransactionType.WITHDRAWALS)
                .setAccountFrom(UUID.fromString(UUID_ACCOUNT_FROM))
                .setSum(1000.40)
                .setCurrency(Currency.EUR)
                .build();
        String expJson = mapper.writeValueAsString(check);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(bufferedReader);
        when(accountService.withdrawalMoney(captorTransaction.capture())).thenReturn(check);

        accountController.doPost(request, response);

        verify(writer).write(captorString.capture());

        Transaction captorTransactionValue = captorTransaction.getValue();

        assertEquals(Currency.EUR, captorTransactionValue.getCurrency());
        assertEquals(UUID.fromString(UUID_ACCOUNT_FROM), captorTransactionValue.getAccountFrom());
        assertEquals(TransactionType.WITHDRAWALS, captorTransactionValue.getType());
        assertEquals(1000.40, captorTransactionValue.getSum());

        String actualJson = captorString.getValue();

        assertEquals(expJson, actualJson);

        verify(accountService, times(1)).withdrawalMoney(any(Transaction.class));
    }

    @Test
    public void doPostAddMoneyCashWhenDataIsValidThenPrintCheck() throws IOException, ServletException {
        String json = String.format("{\"currency\" : \"EUR\",\"account_to\" : \"%s\",\"sum\" : 1000.40, \"type\" : \"Cash replenishment\"}", UUID_ACCOUNT_TO);
        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        Check check = Check.builder()
                .setTransactionType(TransactionType.CASH_REPLENISHMENT)
                .setAccountTo(UUID.fromString(UUID_ACCOUNT_TO))
                .setSum(1000.40)
                .setCurrency(Currency.EUR)
                .build();
        String expJson = mapper.writeValueAsString(check);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(bufferedReader);
        when(accountService.addMoney(captorTransaction.capture())).thenReturn(check);

        accountController.doPost(request, response);

        verify(writer).write(captorString.capture());

        Transaction captorTransactionValue = captorTransaction.getValue();

        assertEquals(Currency.EUR, captorTransactionValue.getCurrency());
        assertEquals(UUID.fromString(UUID_ACCOUNT_TO), captorTransactionValue.getAccountTo());
        assertEquals(TransactionType.CASH_REPLENISHMENT, captorTransactionValue.getType());
        assertEquals(1000.40, captorTransactionValue.getSum());

        String actualJson = captorString.getValue();

        assertEquals(expJson, actualJson);

        verify(accountService, times(1)).addMoney(any(Transaction.class));
    }

    @Test
    public void doPostTransferMoneyWageWhenDataIsValidThenPrintCheck() throws IOException, ServletException {
        String json = String.format("{\"currency\" : \"EUR\",\"account_to\" : \"%s\", \"account_from\" : \"%s\", \"sum\" : 500, \"type\" : \"Wage\"}", UUID_ACCOUNT_TO, UUID_ACCOUNT_FROM);
        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        Check check = Check.builder()
                .setTransactionType(TransactionType.WAGE)
                .setAccountFrom(UUID.fromString(UUID_ACCOUNT_TO))
                .setAccountTo(UUID.fromString(UUID_ACCOUNT_FROM))
                .setSum(500)
                .setCurrency(Currency.EUR)
                .build();
        String expJson = mapper.writeValueAsString(check);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(bufferedReader);
        when(accountService.transferMoney(captorTransaction.capture())).thenReturn(check);

        accountController.doPost(request, response);

        verify(writer).write(captorString.capture());

        Transaction captorTransactionValue = captorTransaction.getValue();

        assertEquals(Currency.EUR, captorTransactionValue.getCurrency());
        assertEquals(UUID.fromString(UUID_ACCOUNT_TO), captorTransactionValue.getAccountTo());
        assertEquals(TransactionType.WAGE, captorTransactionValue.getType());
        assertEquals(500, captorTransactionValue.getSum());

        String actualJson = captorString.getValue();

        assertEquals(expJson, actualJson);

        verify(accountService, times(1)).transferMoney(any(Transaction.class));
    }

    @Test
    public void doPostTransferMoneyWhenDataIsValidThenPrintCheck() throws IOException, ServletException {
        String json = String.format("{\"currency\" : \"EUR\",\"account_to\" : \"%s\", \"account_from\" : \"%s\", \"sum\" : 500, \"type\" : \"Money transfer\"}", UUID_ACCOUNT_TO, UUID_ACCOUNT_FROM);
        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        Check check = Check.builder()
                .setTransactionType(TransactionType.MONEY_TRANSFER)
                .setAccountFrom(UUID.fromString(UUID_ACCOUNT_TO))
                .setAccountTo(UUID.fromString(UUID_ACCOUNT_FROM))
                .setSum(500)
                .setCurrency(Currency.EUR)
                .build();
        String expJson = mapper.writeValueAsString(check);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(bufferedReader);
        when(accountService.transferMoney(any(Transaction.class))).thenReturn(check);

        accountController.doPost(request, response);

        verify(writer).write(captorString.capture());

        String actualJson = captorString.getValue();

        assertEquals(expJson, actualJson);

        verify(accountService, times(1)).transferMoney(any(Transaction.class));
    }

    @Test
    public void doPostTransferMoneyPaymentForServiceWhenDataIsValidThenPrintCheck() throws IOException, ServletException {
        String json = String.format("{\"currency\" : \"EUR\",\"account_to\" : \"%s\", \"account_from\" : \"%s\", \"sum\" : 500, \"type\" : \"Payment for services\"}", UUID_ACCOUNT_TO, UUID_ACCOUNT_FROM);
        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);
        Check check = Check.builder()
                .setTransactionType(TransactionType.PAYMENT_FOR_SERVICES)
                .setAccountFrom(UUID.fromString(UUID_ACCOUNT_TO))
                .setAccountTo(UUID.fromString(UUID_ACCOUNT_FROM))
                .setSum(500)
                .setCurrency(Currency.EUR)
                .build();
        String expJson = mapper.writeValueAsString(check);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(bufferedReader);
        when(accountService.transferMoney(any(Transaction.class))).thenReturn(check);

        accountController.doPost(request, response);

        verify(writer).write(captorString.capture());

        String actualJson = captorString.getValue();

        assertEquals(expJson, actualJson);

        verify(accountService, times(1)).transferMoney(any(Transaction.class));
    }

    @Test
    public void doPostWithdrawalMoneyWhenSumNotValidThenPrintExceptionMessage() throws IOException, ServletException {
        String json = String.format("{\"currency\" : \"EUR\",\"account_from\" : \"%s\",\"sum\" : -1000.40, \"type\" : \"Cash withdrawal\"}", UUID_ACCOUNT_FROM);
        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(bufferedReader);

        accountController.doPost(request, response);

        verify(writer).write(captorString.capture());

        String message = captorString.getValue();

        assertEquals(EXCEPTION_INVALID_SUM, message);

        verify(accountService, times(0)).withdrawalMoney(any(Transaction.class));
        verify(mapper, times(0)).writeValueAsString(any(Check.class));
    }

    @Test
    public void doPostWithdrawalMoneyWhenUUIDNotEnteredThenPrintExceptionMessage() throws IOException, ServletException {
        String json ="{\"currency\" : \"EUR\",\"account_from\" : \"\",\"sum\" : 1000.40, \"type\" : \"Cash withdrawal\"}";
        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(bufferedReader);

        accountController.doPost(request, response);

        verify(writer).write(captorString.capture());

        String message = captorString.getValue();

        assertEquals(EXCEPTION_ACCOUNT_NOT_ENTERED, message);

        verify(accountService, times(0)).withdrawalMoney(any(Transaction.class));
        verify(mapper, times(0)).writeValueAsString(any(Check.class));
    }

    @Test
    public void doPostWithdrawalMoneyWhenUUIDFormatNotValidThenPrintExceptionMessage() throws IOException, ServletException {
        String json ="{\"currency\" : \"EUR\",\"account_from\" : \"hhfjgmj\",\"sum\" : 1000.40, \"type\" : \"Cash withdrawal\"}";
        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(bufferedReader);

        accountController.doPost(request, response);

        verify(writer).write(captorString.capture());

        String message = captorString.getValue();

        assertTrue(message.contains("Cannot deserialize value"));

        verify(accountService, times(0)).withdrawalMoney(any(Transaction.class));
        verify(mapper, times(0)).writeValueAsString(any(Check.class));
    }

    @Test
    public void doPostWhenTransactionTypeNotValidThenPrintExceptionMessage() throws IOException, ServletException {
        String json = String.format("{\"currency\" : \"EUR\",\"account_from\" : \"%s\",\"sum\" : 1000.40, \"type\" : \"Deposit interest\"}", UUID_ACCOUNT_FROM);
        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(bufferedReader);

        accountController.doPost(request, response);

        verify(writer).write(captorString.capture());

        String message = captorString.getValue();

        assertEquals(EXCEPTION_INVALID_OPERATION, message);

        verify(accountService, times(0)).withdrawalMoney(any(Transaction.class));
        verify(mapper, times(0)).writeValueAsString(any(Check.class));
    }

    @Test
    public void doPostCashWhenAccountNotFoundThenPrintExceptionMessage() throws IOException, ServletException {
        String json = String.format("{\"currency\" : \"EUR\",\"account_to\" : \"%s\",\"sum\" : 1000.40, \"type\" : \"Cash replenishment\"}", UUID_ACCOUNT_TO);
        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(bufferedReader);
        when(accountService.addMoney(any(Transaction.class))).thenThrow(new RuntimeException(EXCEPTION_ACCOUNT_NOT_FOUND));

        accountController.doPost(request, response);

        verify(writer).write(captorString.capture());

        assertEquals(EXCEPTION_ACCOUNT_NOT_FOUND, captorString.getValue());

        verify(accountService, times(1)).addMoney(any(Transaction.class));
        verify(mapper, times(0)).writeValueAsString(any(Check.class));
    }
}