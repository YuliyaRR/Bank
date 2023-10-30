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

import static org.example.util.UtilValidator.*;

@WebServlet(name = "ReportController", urlPatterns = "/reports")
public class ReportController extends HttpServlet {
    private IReportService reportService;
    private ObjectMapper mapper;
    private final static String REPORT_DURATION = "duration";
    private final static String REPORT_ACCOUNT = "account";
    private final static String REPORT_TYPE = "report_type";
    private final static String REPORT_DATE_FROM = "from";
    private final static String REPORT_DATE_TO = "to";
    private final static String REPORT_DATE_START = "start";

    @Override
    public void init() throws ServletException {
        this.reportService = ReportServiceSingleton.getInstance();
        this.mapper = ObjectMapperHelperSingleton.getObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();

        try {
            String parameterUUID = req.getParameter(REPORT_ACCOUNT);
            UUID uuid = checkUUIDParameter(parameterUUID);

            String parameterReportType = req.getParameter(REPORT_TYPE);
            checkString(parameterReportType);
            ReportType reportType = ReportType.valueOf(parameterReportType);

            if (reportType.equals(ReportType.ACCOUNT_STATEMENT)) {
                String parameterReportDuration = req.getParameter(REPORT_DURATION);
                checkString(parameterReportDuration);
                Duration duration = Duration.valueOf(parameterReportDuration);

                String dateStart = req.getParameter(REPORT_DATE_START);
                checkString(dateStart);
                LocalDate date = parseDate(dateStart);

                AccountStatement accountStatement = reportService.getAccountStatement(uuid, duration, date);
                writer.write(mapper.writeValueAsString(accountStatement));

            } else if (reportType.equals(ReportType.MONEY_STATEMENT)) {
                String parameterDateFrom = req.getParameter(REPORT_DATE_FROM);
                checkString(parameterDateFrom);
                LocalDate dateFrom = parseDate(parameterDateFrom);

                String parameterDateTo = req.getParameter(REPORT_DATE_TO);
                checkString(parameterDateTo);
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
}
