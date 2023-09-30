package org.example.dao.repositories.api;

import org.example.dao.entity.BankEntity;

import java.util.List;
import java.util.UUID;

public interface IBankRepository {
    BankEntity getBankByAccount(UUID account);
    boolean containsBankWithName(String name);
    boolean containsBankWithUUID(UUID uuid);
    List<BankEntity> getAllBanks();
    void saveBank (BankEntity bank);
    void updateBank(UUID uuid, BankEntity bankEntity);
    void deleteBank(UUID uuid);
}
