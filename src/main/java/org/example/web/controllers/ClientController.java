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

import static org.example.util.UtilValidator.*;

@WebServlet(name = "ClientController", urlPatterns = "/clients")
public class ClientController extends HttpServlet {
    private IClientService clientService;
    private ObjectMapper mapper;
    private final static String CLIENT_ID = "id";

    @Override
    public void init() throws ServletException {
        clientService = ClientServiceSingleton.getInstance();
        mapper = ObjectMapperHelperSingleton.getObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();

        try {
            List<Client> allClients = clientService.getAllClients();
            writer.write(mapper.writeValueAsString(allClients));
        } catch (Exception e) {
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
            Client client = mapper.readValue(req.getReader(), Client.class);
            checkString(client.getName());

            clientService.createClient(client);

        } catch (Exception e) {
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
            String parameterUUID = req.getParameter(CLIENT_ID);
            UUID uuid = checkUUIDParameter(parameterUUID);

            Client client = mapper.readValue(req.getReader(), Client.class);

            checkString(client.getName());

            clientService.updateClient(uuid, client);

        } catch (Exception e) {
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
            String parameterUUID = req.getParameter(CLIENT_ID);
            UUID uuid = checkUUIDParameter(parameterUUID);

            clientService.deleteClient(uuid);

        } catch (Exception e) {
            if (e.getCause() != null) {
                writer.write(e.getMessage() + ": " + e.getCause());
            } else {
                writer.write(e.getMessage());
            }
        }

    }
}
