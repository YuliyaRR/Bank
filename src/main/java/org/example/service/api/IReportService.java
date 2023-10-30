package org.example.service.api;

import org.example.core.dto.docs.AccountStatement;
import org.example.core.dto.Duration;
import org.example.core.dto.docs.MoneyStatement;
import org.example.core.dto.Period;

import java.time.LocalDate;
import java.util.UUID;

public interface IReportService {
    AccountStatement getAccountStatement(UUID account, Duration duration, LocalDate dateLast);
    MoneyStatement getMoneyStatement(UUID account, Period period);

}
