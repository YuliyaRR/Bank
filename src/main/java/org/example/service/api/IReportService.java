package org.example.service.api;

import org.example.core.dto.AccountStatement;
import org.example.core.dto.Duration;

import java.util.UUID;

public interface IReportService {
    AccountStatement getAccountStatement(UUID account, Duration duration);

}
