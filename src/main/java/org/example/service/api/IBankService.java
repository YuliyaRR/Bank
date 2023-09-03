package org.example.service.api;

import org.example.core.dto.Bank;

import java.util.List;
import java.util.UUID;

public interface IBankService {
    Bank getBankByAccount(UUID account);
    List<Bank> getAllBanks();
    void createBank(Bank bank);
    void updateBank(UUID uuid, Bank bank);
    void deleteBank(UUID uuid);

}
