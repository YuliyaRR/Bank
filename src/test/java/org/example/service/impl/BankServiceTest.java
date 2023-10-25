package org.example.service.impl;

import org.example.core.dto.Bank;
import org.example.dao.entity.BankEntity;
import org.example.dao.repositories.api.IBankRepository;
import org.example.service.api.IBankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankServiceTest {
    @Mock
    private IBankRepository bankRepository;
    private IBankService bankService;
    private final static UUID NUM_ACC_EXISTS = UUID.fromString("12605bea-4688-4a8a-b64f-d29e24eb6d81");
    private final static UUID NUM_ACC_NOT_EXIST = UUID.fromString("02605bea-4688-4a8a-b64f-d29e24eb6d81");
    private final static String NAME_BANK = "Test Bank";
    private final static String NAME_NEW_BANK = "New Bank";
    private final static String EXCEPTION_NO_BANKS_IN_SYSTEM = "There are no banks registered in the system";
    private final static String EXCEPTION_NAME_NOT_UNIQUE = "Bank with the same name already exists";
    private final static String EXCEPTION_BANK_NOT_FOUND = "Bank not found";
    @BeforeEach
    public void setUp() {
        this.bankService = new BankService(bankRepository);
    }

    @Test
    public void getBankByAccount() {
        BankEntity fromdao = new BankEntity(NUM_ACC_EXISTS, NAME_BANK);
        when(bankRepository.getBankByAccount(any(UUID.class))).thenReturn(fromdao);

        Bank bank = bankService.getBankByAccount(NUM_ACC_EXISTS);

        verify(bankRepository, times(1)).getBankByAccount(any(UUID.class));

        assertEquals(fromdao.getId(), bank.getId());
        assertEquals(fromdao.getName(), bank.getName());
    }

    @Test
    public void getAllBanks() {
        BankEntity fromdaoA = new BankEntity(UUID.randomUUID(), "Test Bank 1");
        BankEntity fromdaoB = new BankEntity(UUID.randomUUID(), "Test Bank 2");
        BankEntity fromdaoC = new BankEntity(UUID.randomUUID(), "Test Bank 3");

        List<BankEntity> fromdaoList = List.of(fromdaoA, fromdaoB, fromdaoC);

        when(bankRepository.getAllBanks()).thenReturn(fromdaoList);

        List<Bank> allBanks = bankService.getAllBanks();

        List<Bank> expectedBankList = fromdaoList.stream()
                .map(entity -> new Bank(entity.getId(), entity.getName()))
                .toList();

        assertNotNull(allBanks);
        assertFalse(allBanks.isEmpty());
        assertEquals(expectedBankList, allBanks);

        verify(bankRepository, times(1)).getAllBanks();
    }

    @Test
    public void getAllBanksWhenNoBanksInTheSystemThenThrowException() {
        List<BankEntity> fromdaoList = new ArrayList<>();

        when(bankRepository.getAllBanks()).thenReturn(fromdaoList);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> bankService.getAllBanks());
        assertEquals(EXCEPTION_NO_BANKS_IN_SYSTEM, exception.getMessage());

        verify(bankRepository, times(1)).getAllBanks();
    }


    @Test
    public void createBankWhenNameUniqueThenBankSaveInDB() {
        Bank newBank = new Bank(NAME_NEW_BANK);
        when(bankRepository.containsBankWithName(NAME_NEW_BANK)).thenReturn(false);

        bankService.createBank(newBank);

        verify(bankRepository, times(1)).containsBankWithName(NAME_NEW_BANK);
        verify(bankRepository, times(1)).saveBank(any(BankEntity.class));
    }

    @Test
    public void createBankWhenNameNotUniqueThenThrowException() {
        Bank newBankNotUnique = new Bank(NAME_BANK);
        when(bankRepository.containsBankWithName(NAME_BANK)).thenReturn(true);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> bankService.createBank(newBankNotUnique));
        assertEquals(EXCEPTION_NAME_NOT_UNIQUE, runtimeException.getMessage());

        verify(bankRepository, times(1)).containsBankWithName(NAME_BANK);
        verify(bankRepository, times(0)).saveBank(any(BankEntity.class));
    }

    @Test
    public void updateBankWhenBankExistAndNameUniqueThenBankUpdateInDB() {
        when(bankRepository.containsBankWithUUID(NUM_ACC_EXISTS)).thenReturn(true);
        when(bankRepository.containsBankWithName(NAME_NEW_BANK)).thenReturn(false);

        bankService.updateBank(NUM_ACC_EXISTS, new Bank(NAME_NEW_BANK));

        verify(bankRepository, times(1)).containsBankWithUUID(NUM_ACC_EXISTS);
        verify(bankRepository, times(1)).containsBankWithName(NAME_NEW_BANK);
        verify(bankRepository, times(1)).updateBank(NUM_ACC_EXISTS, new BankEntity(NUM_ACC_EXISTS, NAME_NEW_BANK));
    }

    @Test
    public void updateBankWhenBankNotExistThenThrowException() {
        when(bankRepository.containsBankWithUUID(NUM_ACC_NOT_EXIST)).thenReturn(false);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> bankService.updateBank(NUM_ACC_NOT_EXIST, new Bank(NAME_NEW_BANK)));
        assertEquals(EXCEPTION_BANK_NOT_FOUND, runtimeException.getMessage());

        verify(bankRepository, times(1)).containsBankWithUUID(NUM_ACC_NOT_EXIST);
        verify(bankRepository, times(0)).containsBankWithName(NAME_NEW_BANK);
        verify(bankRepository, times(0)).updateBank(NUM_ACC_NOT_EXIST, new BankEntity(NUM_ACC_NOT_EXIST, NAME_NEW_BANK));
    }

    @Test
    public void updateBankWhenBankExistButNameNotUniqueThenThrowException() {
        when(bankRepository.containsBankWithUUID(NUM_ACC_EXISTS)).thenReturn(true);
        when(bankRepository.containsBankWithName(NAME_BANK)).thenReturn(true);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> bankService.updateBank(NUM_ACC_EXISTS, new Bank(NAME_BANK)));
        assertEquals(EXCEPTION_NAME_NOT_UNIQUE, runtimeException.getMessage());

        verify(bankRepository, times(1)).containsBankWithUUID(NUM_ACC_EXISTS);
        verify(bankRepository, times(1)).containsBankWithName(NAME_BANK);
        verify(bankRepository, times(0)).updateBank(NUM_ACC_EXISTS, new BankEntity(NUM_ACC_EXISTS, NAME_BANK));
    }

    @Test
    public void deleteBankWhenBankExistThenDeleteFromDB() {
        when(bankRepository.containsBankWithUUID(NUM_ACC_EXISTS)).thenReturn(true);

        bankService.deleteBank(NUM_ACC_EXISTS);

        verify(bankRepository, times(1)).containsBankWithUUID(NUM_ACC_EXISTS);
        verify(bankRepository, times(1)).deleteBank(NUM_ACC_EXISTS);
    }

    @Test
    public void deleteBankWhenBankNotExistThrowException() {
        when(bankRepository.containsBankWithUUID(NUM_ACC_EXISTS)).thenReturn(false);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> bankService.deleteBank(NUM_ACC_EXISTS));
        assertEquals(EXCEPTION_BANK_NOT_FOUND, runtimeException.getMessage());

        verify(bankRepository, times(1)).containsBankWithUUID(NUM_ACC_EXISTS);
        verify(bankRepository, times(0)).deleteBank(NUM_ACC_EXISTS);
    }
}