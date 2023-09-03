package org.example.service.api;

import org.example.core.dto.Bank;

import java.util.UUID;

public interface IBankService {
    Bank getBankByAccount(UUID account);
}
