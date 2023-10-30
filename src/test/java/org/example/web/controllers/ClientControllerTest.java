package org.example.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.dto.Client;

import org.example.service.api.IClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {
    @InjectMocks
    private ClientController clientController;
    @Mock
    private IClientService clientService;
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
    private ArgumentCaptor<Client> captorClient;
    private static final String CLIENT_ID = "12605bea-4688-4a8a-b64f-d29e24eb6d81";
    private static final String PARAMETER_CLIENT_ID = "id";
    private static final String EXCEPTION_NO_CLIENTS = "There are no clients registered in the system";
    private static final String EXCEPTION_NO_DATA = "Data not entered";
    private static final String EXCEPTION_INVALID_UUID = "UUID not valid";

    @Test
    public void doGetAllClientsSuccessfully() throws ServletException, IOException {
        Client clientA = new Client(UUID.randomUUID(), "Client 1");
        Client clientB = new Client(UUID.randomUUID(), "Client 2");
        Client clientC = new Client(UUID.randomUUID(), "Client 3");

        List<Client> clients = new ArrayList<>(List.of(clientA, clientB, clientC));

        String expJson = mapper.writeValueAsString(clients);

        when(response.getWriter()).thenReturn(writer);
        when(clientService.getAllClients()).thenReturn(clients);

        clientController.doGet(request, response);

        verify(writer).write(captorString.capture());
        verify(clientService, times(1)).getAllClients();

        String respJson = captorString.getValue();

        assertEquals(expJson, respJson);
    }

    @Test
    public void doGetAllClientsWhenNoClientsInTheSystemThenPrintExceptionMessage() throws ServletException, IOException {
        when(response.getWriter()).thenReturn(writer);
        when(clientService.getAllClients()).thenThrow(new RuntimeException(EXCEPTION_NO_CLIENTS));

        clientController.doGet(request, response);

        verify(writer).write(captorString.capture());

        assertEquals(EXCEPTION_NO_CLIENTS, captorString.getValue());

        verify(clientService, times(1)).getAllClients();
        verify(mapper, times(0)).writeValueAsString(any());
    }

    @Test
    public void doPostCreateClientWhenNameValid() throws IOException, ServletException {
        String json = "{\"name\" : \"Client 1\"}";
        Client expectedClient = new Client("Client 1");

        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(bufferedReader);

        clientController.doPost(request, response);

        verify(clientService).createClient(captorClient.capture());

        assertEquals(expectedClient, captorClient.getValue());

        verify(writer, times(0)).write(any(String.class));
        verify(clientService, times(1)).createClient(any(Client.class));
    }

    @Test
    public void doPostCreateClientWhenNameIsEmptyThenPrintExceptionMessage() throws IOException, ServletException {
        String json = "{\"name\" : \"\"}";

        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(bufferedReader);

        clientController.doPost(request, response);

        verify(writer).write(captorString.capture());
        assertEquals(EXCEPTION_NO_DATA, captorString.getValue());

        verify(writer, times(1)).write(any(String.class));
        verify(clientService, times(0)).createClient(any(Client.class));
    }

    @Test
    public void doPostCreateClientWhenNameIsBlankThenPrintExceptionMessage() throws IOException, ServletException {
        String json = "{\"name\" : \"  \"}";

        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(bufferedReader);

        clientController.doPost(request, response);

        verify(writer).write(captorString.capture());
        assertEquals(EXCEPTION_NO_DATA, captorString.getValue());

        verify(writer, times(1)).write(any(String.class));
        verify(clientService, times(0)).createClient(any(Client.class));
    }

    @Test
    public void doPutUpdateClientWhenDataIsValid() throws IOException, ServletException {
        String json = "{\"name\" : \"Client Upd\"}";
        Client expectedClient = new Client("Client Upd");
        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_CLIENT_ID)).thenReturn(CLIENT_ID);
        when(request.getReader()).thenReturn(bufferedReader);

        clientController.doPut(request, response);

        verify(clientService).updateClient(any(UUID.class), captorClient.capture());

        assertEquals(expectedClient, captorClient.getValue());

        verify(writer, times(0)).write(any(String.class));
        verify(clientService, times(1)).updateClient(any(UUID.class), any(Client.class));
    }

    @Test
    public void doPutUpdateClientWhenUUIDNotValidThenPrintExceptionMessage() throws IOException, ServletException {
        String uuidInvalid = "12605bea-4688-a8a-b64f-d29e24eb6d81";

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_CLIENT_ID)).thenReturn(uuidInvalid);

        clientController.doPut(request, response);

        verify(writer).write(captorString.capture());
        assertEquals(EXCEPTION_INVALID_UUID, captorString.getValue());

        verify(writer, times(1)).write(any(String.class));
        verify(clientService, times(0)).updateClient(any(UUID.class), any(Client.class));
    }

    @Test
    public void doPutUpdateClientWhenNameIsEmptyThenPrintExceptionMessage() throws IOException, ServletException {
        String json = "{\"name\" : \"\"}";
        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_CLIENT_ID)).thenReturn(CLIENT_ID);
        when(request.getReader()).thenReturn(bufferedReader);

        clientController.doPut(request, response);

        verify(writer).write(captorString.capture());
        assertEquals(EXCEPTION_NO_DATA, captorString.getValue());

        verify(writer, times(1)).write(any(String.class));
        verify(clientService, times(0)).updateClient(any(UUID.class), any(Client.class));
    }

    @Test
    public void doPutUpdateClientWhenNameIsBlankThenPrintExceptionMessage() throws IOException, ServletException {
        String json = "{\"name\" : \"  \"}";
        StringReader reader = new StringReader(json);
        BufferedReader bufferedReader = new BufferedReader(reader);

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_CLIENT_ID)).thenReturn(CLIENT_ID);
        when(request.getReader()).thenReturn(bufferedReader);

        clientController.doPut(request, response);

        verify(writer).write(captorString.capture());
        assertEquals(EXCEPTION_NO_DATA, captorString.getValue());

        verify(writer, times(1)).write(any(String.class));
        verify(clientService, times(0)).updateClient(any(UUID.class), any(Client.class));
    }

    @Test
    public void doDeleteClientWhenUUIDIsValidAndClientWasFound() throws IOException, ServletException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_CLIENT_ID)).thenReturn(CLIENT_ID);

        clientController.doDelete(request, response);

        verify(writer, times(0)).write(any(String.class));
        verify(clientService, times(1)).deleteClient(any(UUID.class));
    }

    @Test
    public void doDeleteClientWhenUUIDNotValidThenPrintExceptionMessage() throws IOException, ServletException {
        String uuidInvalid = "12605bea-4688-a8a-b64f-d29e24eb6d81";

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_CLIENT_ID)).thenReturn(uuidInvalid);

        clientController.doDelete(request, response);

        verify(writer).write(captorString.capture());
        assertEquals(EXCEPTION_INVALID_UUID, captorString.getValue());

        verify(writer, times(1)).write(any(String.class));
        verify(clientService, times(0)).deleteClient(any(UUID.class));
    }

    @Test
    public void doDeleteClientWhenUUIDIsValidAndClientNotFoundThenPrintExceptionMessage() throws IOException, ServletException {
        UUID uuid = UUID.fromString(CLIENT_ID);
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter(PARAMETER_CLIENT_ID)).thenReturn(CLIENT_ID);
        doThrow(new RuntimeException(EXCEPTION_NO_CLIENTS)).when(clientService).deleteClient(uuid);

        clientController.doDelete(request, response);

        verify(writer).write(captorString.capture());
        assertEquals(EXCEPTION_NO_CLIENTS, captorString.getValue());

        verify(writer, times(1)).write(any(String.class));
        verify(clientService, times(1)).deleteClient(any(UUID.class));
    }
}
