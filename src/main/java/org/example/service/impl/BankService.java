package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.dto.Bank;
import org.example.dao.entity.BankEntity;
import org.example.dao.repositories.api.IBankRepository;
import org.example.service.api.IBankService;

import java.util.List;
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

    /**
     * Метод возвращает информацию обо всех банках
     * @return список банков
     */
    @Override
    public List<Bank> getAllBanks() {
        List<BankEntity> allBanks = bankRepository.getAllBanks();

        if(allBanks.isEmpty()) {
            throw new RuntimeException("There are no banks registered in the system");
        }
        return allBanks.stream()
                .map(bankEntity -> convertEntityToDto(bankEntity))
                .toList();
    }

    /** Метод передает информацию о банке для сохранения в БД
     * @param bank объект-источник информации о новом банке, на данный момент это имя нового банка
     */
    @Override
    public void createBank(Bank bank) {
        String name = bank.getName();
        checkNameUnique(name);
        bankRepository.saveBank(new BankEntity(UUID.randomUUID(), name));
    }

    /**
     * Метод передает данные для обновления информации о банке на БД
     * @param uuid идентификатор банка, информация о котором будет обновлена
     * @param bank объект-источник информации о новом банке, на данный момент это новое имя банка
     */
    @Override
    public void updateBank(UUID uuid, Bank bank) {
        String name = bank.getName();
        checkBankExistenceByUuid(uuid);
        checkNameUnique(name);
        bankRepository.updateBank(uuid, new BankEntity(uuid, name));
    }

    /**
     * Метод передает данные для удаления банка из БД
     * @param uuid идентификатор банка, который будет удален
     */
    @Override
    public void deleteBank(UUID uuid) {
        checkBankExistenceByUuid(uuid);
        bankRepository.deleteBank(uuid);
    }

    /** Метод конвертирует BankEntity в Bank
     * @param entity объект-источник
     * @return Bank
     */
    private Bank convertEntityToDto(BankEntity entity) {
        return new Bank(entity.getId(), entity.getName());
    }

    /** Метод проверяет уникальность переданного имени
     * @param name имя, которое будет присвоено банку
     */
    private void checkNameUnique(String name) {
       if (bankRepository.containsBankWithName(name)) {
           throw new RuntimeException("Bank with the same name already exists");
       }
    }

    /** Метод проверяет существует ли банк с переданным идентификатором
     * @param uuid идентификатор банка
     */
    private void checkBankExistenceByUuid(UUID uuid) {
        if (!bankRepository.containsBankWithUUID(uuid)) {
            throw new RuntimeException("Bank not found");
        }
    }
}
