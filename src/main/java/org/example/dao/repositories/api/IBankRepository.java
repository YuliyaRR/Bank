package org.example.dao.repositories.api;

import org.example.dao.entity.BankEntity;

import java.util.UUID;

public interface IBankRepository {
    BankEntity getBankByAccount(UUID account);
}
