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

@WebServlet(name = "BankController", urlPatterns = "/banks")
public class BankController extends HttpServlet {
    private final IBankService bankService;
    private final ObjectMapper mapper;
    private final static String BANK_UUID = "uuid";

    public BankController() {
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
            Bank bank = mapper.readValue(req.getInputStream(), Bank.class);
            checkString(bank.getName());

            bankService.createBank(bank);

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
            UUID uuid = UUID.fromString(req.getParameter(BANK_UUID));
            Bank bank = mapper.readValue(req.getInputStream(), Bank.class);
            checkString(bank.getName());

            bankService.updateBank(uuid, bank);

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
            UUID uuid = UUID.fromString(req.getParameter(BANK_UUID));

            bankService.deleteBank(uuid);

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
