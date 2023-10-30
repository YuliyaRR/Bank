package org.example.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.dto.Bank;
import org.example.service.api.IBankService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankControllerTest {
    @InjectMocks
    private BankController bankController;
    @Mock
    private IBankService bankService;
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
    @Captor
    private ArgumentCaptor<Bank> captorBank;
    private static final String PARAMETER_BANK_ID = "uuid";
    private static final String BANK_ID = "12605bea-4688-4a8a-b64f-d29e24eb6d81";
    private static final String EXCEPTION_NO_BANKS = "There are no banks registered in the system";
    private static final String EXCEPTION_NO_DATA = "Data not entered";
    private static final String EXCEPTION_INVALID_UUID = "UUID not valid";

    @Test
    public void doGetAllBanksSuccessfully() throws ServletException, IOException {
        Bank bankA = new Bank(UUID.randomUUID(), "Bank 1");
        Bank bankB = new Bank(UUID.randomUUID(), "Bank 2");
        Bank bankC = new Bank(UUID.randomUUID(), "Bank 3");

        List<Bank> banks = new ArrayList<>(List.of(bankA, bankB, bankC));

        String expJson = mapper.writeValueAsString(banks);

        when(response.getWriter()).thenReturn(writer);
        when(bankService.getAllBanks()).thenReturn(banks);

        bankController.doGet(request, response);

        verify(writer).write(captorString.capture());
        verify(bankService, times(1)).getAllBanks();

        String respJson = captorString.getValue();

        assertEquals(expJson, respJson);
    }

    @Test
    public void doGetAllBanksWhenNoBanksInTheSystemThenPrintExceptionMessage() throws ServletException, IOException {
        when(response.getWriter()).thenReturn(writer);
        when(bankService.getAllBanks()).thenThrow(new RuntimeException(EXCEPTION_NO_BANKS));

        bankController.doGet(request, response);

        verify(writer).write(captorString.capture());

        assertEquals(EXCEPTION_NO_BANKS, captorString.getValue());

        verify(bankService, times(1)).getAllBanks();
        verify(mapper, times(0)).writeValueAsString(any());
    }

    @Test
    public void doPostCreateBankWhenNameValid() throws IOException, ServletException {
        String json = "{\"name\" : \"Bank 1\"}";
        Bank expectedBank = new Bank("Bank 1");

        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(bufferedReader);

        bankController.doPost(request, response);

        verify(bankService).createBank(captorBank.capture());

        assertEquals(expectedBank, captorBank.getValue());

        verify(writer, times(0)).write(any(String.class));
        verify(bankService, times(1)).createBank(any(Bank.class));
    }

    @Test
    public void doPostCreateBankWhenNameIsEmptyThenPrintExceptionMessage() throws IOException, ServletException {
        String json = "{\"name\" : \"\"}";

        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(bufferedReader);

        bankController.doPost(request, response);

        verify(writer).write(captorString.capture());
        assertEquals(EXCEPTION_NO_DATA, captorString.getValue());

        verify(writer, times(1)).write(any(String.class));
        verify(bankService, times(0)).createBank(any(Bank.class));
    }

    @Test
    public void doPostCreateBankWhenNameIsBlankThenPrintExceptionMessage() throws IOException, ServletException {
        String json = "{\"name\" : \"  \"}";

        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(bufferedReader);

        bankController.doPost(request, response);

        verify(writer).write(captorString.capture());
        assertEquals(EXCEPTION_NO_DATA, captorString.getValue());

        verify(writer, times(1)).write(any(String.class));
        verify(bankService, times(0)).createBank(any(Bank.class));
    }

    @Test
    public void doPutUpdateBankWhenDataIsValid() throws IOException, ServletException {
        String json = "{\"name\" : \"Bank Upd\"}";
        Bank expectedBank = new Bank("Bank Upd");
        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_BANK_ID)).thenReturn(BANK_ID);
        when(request.getReader()).thenReturn(bufferedReader);

        bankController.doPut(request, response);

        verify(bankService).updateBank(any(UUID.class), captorBank.capture());

        assertEquals(expectedBank, captorBank.getValue());

        verify(writer, times(0)).write(any(String.class));
        verify(bankService, times(1)).updateBank(any(UUID.class), any(Bank.class));
    }

    @Test
    public void doPutUpdateBankWhenUUIDNotValidThenPrintExceptionMessage() throws IOException, ServletException {
        String uuidInvalid = "12605bea-4688-a8a-b64f-d29e24eb6d81";

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_BANK_ID)).thenReturn(uuidInvalid);

        bankController.doPut(request, response);

        verify(writer).write(captorString.capture());
        assertEquals(EXCEPTION_INVALID_UUID, captorString.getValue());

        verify(writer, times(1)).write(any(String.class));
        verify(bankService, times(0)).updateBank(any(UUID.class), any(Bank.class));
    }

    @Test
    public void doPutUpdateBankWhenNameIsEmptyThenPrintExceptionMessage() throws IOException, ServletException {
        String json = "{\"name\" : \"\"}";
        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_BANK_ID)).thenReturn(BANK_ID);
        when(request.getReader()).thenReturn(bufferedReader);

        bankController.doPut(request, response);

        verify(writer).write(captorString.capture());
        assertEquals(EXCEPTION_NO_DATA, captorString.getValue());

        verify(writer, times(1)).write(any(String.class));
        verify(bankService, times(0)).updateBank(any(UUID.class), any(Bank.class));
    }

    @Test
    public void doPutUpdateBankWhenNameIsBlankThenPrintExceptionMessage() throws IOException, ServletException {
        String json = "{\"name\" : \"  \"}";
        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_BANK_ID)).thenReturn(BANK_ID);
        when(request.getReader()).thenReturn(bufferedReader);

        bankController.doPut(request, response);

        verify(writer).write(captorString.capture());
        assertEquals(EXCEPTION_NO_DATA, captorString.getValue());

        verify(writer, times(1)).write(any(String.class));
        verify(bankService, times(0)).updateBank(any(UUID.class), any(Bank.class));
    }

    @Test
    public void doDeleteBankWhenUUIDIsValidAndClientWasFound() throws IOException, ServletException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_BANK_ID)).thenReturn(BANK_ID);

        bankController.doDelete(request, response);

        verify(writer, times(0)).write(any(String.class));
        verify(bankService, times(1)).deleteBank(any(UUID.class));
    }

    @Test
    public void doDeleteBankWhenUUIDNotValidThenPrintExceptionMessage() throws IOException, ServletException {
        String uuidInvalid = "12605bea-4688-a8a-b64f-d29e24eb6d81";

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_BANK_ID)).thenReturn(uuidInvalid);

        bankController.doDelete(request, response);

        verify(writer).write(captorString.capture());
        assertEquals(EXCEPTION_INVALID_UUID, captorString.getValue());

        verify(writer, times(1)).write(any(String.class));
        verify(bankService, times(0)).deleteBank(any(UUID.class));
    }

    @Test
    public void doDeleteBankWhenUUIDIsValidAndClientNotFoundThenPrintExceptionMessage() throws IOException, ServletException {
        UUID uuid = UUID.fromString(BANK_ID);
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_BANK_ID)).thenReturn(BANK_ID);
        doThrow(new RuntimeException(EXCEPTION_NO_BANKS)).when(bankService).deleteBank(uuid);

        bankController.doDelete(request, response);

        verify(writer).write(captorString.capture());
        assertEquals(EXCEPTION_NO_BANKS, captorString.getValue());

        verify(writer, times(1)).write(any(String.class));
        verify(bankService, times(1)).deleteBank(any(UUID.class));
    }
}