package org.example.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.dto.Client;
import org.example.service.api.IClientService;
import org.example.service.factory.ClientServiceSingleton;
import org.example.service.factory.ObjectMapperHelperSingleton;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "ClientController", urlPatterns = "/clients")
public class ClientController extends HttpServlet {
    private final IClientService clientService;
    private final ObjectMapper mapper;
    private final static String CLIENT_ID = "id";

    public ClientController() {
        this.clientService = ClientServiceSingleton.getInstance();
        this.mapper = ObjectMapperHelperSingleton.getObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();

        try {
            List<Client> allClients = clientService.getAllClients();
            writer.write(mapper.writeValueAsString(allClients));

        } catch (RuntimeException e) {
            if (e.getCause() != null) {
                writer.write(e.getMessage() + ": " + e.getCause());
            } else {
                writer.write(e.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();
        try{
            Client client = mapper.readValue(req.getInputStream(), Client.class);
            checkString(client.getName());

            clientService.createClient(client);

        } catch (RuntimeException e) {
            if (e.getCause() != null) {
                writer.write(e.getMessage() + ": " + e.getCause());
            } else {
                writer.write(e.getMessage());
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();

        try {
            UUID uuid = UUID.fromString(req.getParameter(CLIENT_ID));
            Client client = mapper.readValue(req.getInputStream(), Client.class);
            checkString(client.getName());

            clientService.updateClient(uuid, client);

        } catch (RuntimeException e) {
            if (e.getCause() != null) {
                writer.write(e.getMessage() + ": " + e.getCause());
            } else {
                writer.write(e.getMessage());
            }
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();
        try {
            UUID uuid = UUID.fromString(req.getParameter(CLIENT_ID));

            clientService.deleteClient(uuid);

        } catch (RuntimeException e) {
            if (e.getCause() != null) {
                writer.write(e.getMessage() + ": " + e.getCause());
            } else {
                writer.write(e.getMessage());
            }
        }

    }

    private void checkString(String str) {
        if(str.isEmpty() || str.isBlank()) {
            throw new RuntimeException("Data not entered");
        }
    }
}
