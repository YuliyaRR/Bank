package org.example.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.dto.*;
import org.example.core.dto.docs.AccountStatement;
import org.example.core.dto.docs.MoneyStatement;
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
import java.time.LocalDate;
import java.util.UUID;

@WebServlet(name = "ReportController", urlPatterns = "/reports")
public class ReportController extends HttpServlet {
    private final IReportService reportService;
    private final ObjectMapper mapper;
    private final static String REPORT_DURATION = "duration";
    private final static String REPORT_ACCOUNT = "account";
    private final static String REPORT_TYPE = "report_type";
    private final static String REPORT_DATE_FROM = "from";
    private final static String REPORT_DATE_TO = "to";
    private final static String REPORT_DATE_START = "start";


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
            UUID uuid = UUID.fromString(req.getParameter(REPORT_ACCOUNT));
            ReportType reportType = ReportType.valueOf(req.getParameter(REPORT_TYPE));

            if (reportType.equals(ReportType.ACCOUNT_STATEMENT)) {
                Duration duration = Duration.valueOf(req.getParameter(REPORT_DURATION));

                String dateStart = req.getParameter(REPORT_DATE_START);
                LocalDate date = parseDate(dateStart);

                AccountStatement accountStatement = reportService.getAccountStatement(uuid, duration, date);
                writer.write(mapper.writeValueAsString(accountStatement));

            } else if (reportType.equals(ReportType.MONEY_STATEMENT)) {
                String parameterDateFrom = req.getParameter(REPORT_DATE_FROM);
                LocalDate dateFrom = parseDate(parameterDateFrom);

                String parameterDateTo = req.getParameter(REPORT_DATE_TO);
                LocalDate dateTo = parseDate(parameterDateTo);

                Period period = new Period(dateFrom, dateTo);

                MoneyStatement moneyStatement = reportService.getMoneyStatement(uuid, period);
                writer.write(mapper.writeValueAsString(moneyStatement));
            }
        } catch (RuntimeException e) {
            if (e.getCause() != null) {
                writer.write(e.getMessage() + ": " + e.getCause());
            } else {
                writer.write(e.getMessage());
            }
        }
    }

    private LocalDate parseDate(String date) {
        if (date == null) {
            throw new IllegalArgumentException("The date of the reporting period is not specified");
        }
        return LocalDate.parse(date);
    }
}
