package org.example.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.dto.docs.Check;
import org.example.core.dto.Transaction;
import org.example.service.api.IAccountService;
import org.example.service.factory.AccountServiceSingleton;
import org.example.service.factory.ObjectMapperHelperSingleton;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.example.util.UtilValidator.*;

@WebServlet(name = "AccountController", urlPatterns = "/accounts")
public class AccountController extends HttpServlet {
    private IAccountService accountService;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        this.accountService = AccountServiceSingleton.getInstance();
        this.mapper = ObjectMapperHelperSingleton.getObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();

        try {
            Transaction transaction = mapper.readValue(req.getReader(), Transaction.class);

            double sum = transaction.getSum();

            if(sum <= 0) {
                throw new RuntimeException("The amount of any transaction must be greater than zero");
            }

            Check check;

            switch (transaction.getType()) {
                case WITHDRAWALS -> {
                    checkUUIDAccountBody(transaction.getAccountFrom());
                    check = accountService.withdrawalMoney(transaction);
                }
                case CASH_REPLENISHMENT -> {
                    checkUUIDAccountBody(transaction.getAccountTo());
                    check = accountService.addMoney(transaction);
                }
                case WAGE, MONEY_TRANSFER, PAYMENT_FOR_SERVICES -> {
                    checkUUIDAccountBody(transaction.getAccountFrom());
                    checkUUIDAccountBody(transaction.getAccountTo());
                    check = accountService.transferMoney(transaction);
                }
                default -> throw new RuntimeException("The requested operations aren't currently supported");
            }

            writer.write(mapper.writeValueAsString(check));

        } catch (Exception e) {
            if (e.getCause() != null) {
                writer.write(e.getMessage() + ": " + e.getCause());
            } else {
                writer.write(e.getMessage());
            }
        }
    }
}
