package org.example.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.dto.Bank;
import org.example.service.api.IBankService;
import org.example.service.factory.BankServiceSingleton;
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

@WebServlet(name = "BankController", urlPatterns = "/banks")
public class BankController extends HttpServlet {
    private IBankService bankService;
    private ObjectMapper mapper;
    private final static String BANK_UUID = "uuid";

    @Override
    public void init() throws ServletException {
        this.bankService = BankServiceSingleton.getInstance();
        this.mapper = ObjectMapperHelperSingleton.getObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();

        try {
            List<Bank> allBanks = bankService.getAllBanks();
            writer.write(mapper.writeValueAsString(allBanks));

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
            Bank bank = mapper.readValue(req.getReader(), Bank.class);
            checkString(bank.getName());

            bankService.createBank(bank);

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
            String parameterUUID = req.getParameter(BANK_UUID);
            UUID uuid = checkUUIDParameter(parameterUUID);

            Bank bank = mapper.readValue(req.getReader(), Bank.class);
            checkString(bank.getName());

            bankService.updateBank(uuid, bank);

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
            String parameterUUID = req.getParameter(BANK_UUID);
            UUID uuid = checkUUIDParameter(parameterUUID);

            bankService.deleteBank(uuid);

        } catch (Exception e) {
            if (e.getCause() != null) {
                writer.write(e.getMessage() + ": " + e.getCause());
            } else {
                writer.write(e.getMessage());
            }
        }
    }
}
