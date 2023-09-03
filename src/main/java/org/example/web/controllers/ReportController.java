package org.example.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.dto.AccountStatement;
import org.example.core.dto.Duration;
import org.example.service.api.IReportService;
import org.example.service.factory.ObjectMapperHelperSingleton;
import org.example.service.factory.ReportServiceSingleton;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@WebServlet(name = "ReportController", urlPatterns = "/reports")
public class ReportController extends HttpServlet {
    private final IReportService reportService;
    private final ObjectMapper mapper;
    private final static String REPORT_DURATION = "duration";
    private final static String REPORT_ACCOUNT = "account";


    public ReportController() {
        this.reportService = ReportServiceSingleton.getInstance();
        this.mapper = ObjectMapperHelperSingleton.getObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();

        try {
            Duration duration = Duration.valueOf(req.getParameter(REPORT_DURATION));
            UUID uuid = UUID.fromString(req.getParameter(REPORT_ACCOUNT));

            AccountStatement accountStatement = reportService.getAccountStatement(uuid, duration);

            writer.write(mapper.writeValueAsString(accountStatement));

        } catch (RuntimeException e) {
            if (e.getCause() != null) {
                writer.write(e.getMessage() + ": " + e.getCause());
            } else {
                writer.write(e.getMessage());
            }
        }



    }
}
