package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.dto.Bank;
import org.example.dao.entity.BankEntity;
import org.example.dao.repositories.api.IBankRepository;
import org.example.service.api.IBankService;

import java.util.UUID;

@RequiredArgsConstructor
public class BankService implements IBankService {
    private final IBankRepository bankRepository;

    /** Метод предоставляет информацию о банке, в котором открыт счет.
     * @param account счет
     * @return Bank, содержит информацию о банке, в котором открыт счет
     */
    @Override
    public Bank getBankByAccount(UUID account) {
        BankEntity entity = bankRepository.getBankByAccount(account);
        return new Bank(entity.getId(), entity.getName());
    }
}
