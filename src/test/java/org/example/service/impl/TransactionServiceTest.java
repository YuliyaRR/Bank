package org.example.service.impl;

import org.example.core.dto.*;
import org.example.dao.entity.TransactionEntity;
import org.example.dao.repositories.api.ITransactionRepository;
import org.example.service.api.ITransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private ITransactionRepository transactionRepository;
    private ITransactionService transactionService;

    @BeforeEach
    public void setUp() {
        this.transactionService = new TransactionService(transactionRepository);
    }

    @Test
    public void saveTransactionThenInvokeTransactionRepo1Time() {
        Transaction transaction = Transaction.builder()
                .setId(UUID.randomUUID())
                .setCurrency(Currency.RUB)
                .setDate(LocalDateTime.now())
                .setAccountFrom(UUID.randomUUID())
                .setAccountTo(UUID.randomUUID())
                .setSum(100)
                .setType(TransactionType.MONEY_TRANSFER)
                .build();

        transactionService.saveTransaction(transaction);

        verify(transactionRepository, times(1)).saveTransaction(any(TransactionEntity.class));
    }

    @Test
    public void saveMonthlyInterestTransactionsThenInvokeTransactionRepo1Time() {
        Transaction transaction = Transaction.builder()
                .setId(UUID.randomUUID())
                .setCurrency(Currency.RUB)
                .setDate(LocalDateTime.now())
                .setAccountFrom(UUID.randomUUID())
                .setAccountTo(UUID.randomUUID())
                .setSum(100)
                .setType(TransactionType.MONEY_TRANSFER)
                .build();

        Transaction transaction2 = Transaction.builder()
                .setId(UUID.randomUUID())
                .setCurrency(Currency.USD)
                .setDate(LocalDateTime.now())
                .setAccountFrom(UUID.randomUUID())
                .setAccountTo(UUID.randomUUID())
                .setSum(-100)
                .setType(TransactionType.WAGE)
                .build();

        Transaction transaction3 = Transaction.builder()
                .setId(UUID.randomUUID())
                .setCurrency(Currency.BYN)
                .setDate(LocalDateTime.now())
                .setAccountFrom(UUID.randomUUID())
                .setAccountTo(UUID.randomUUID())
                .setSum(1300)
                .setType(TransactionType.PAYMENT_FOR_SERVICES)
                .build();

        List<Transaction> list = new ArrayList<>(List.of(transaction, transaction2, transaction3));

        transactionService.saveMonthlyInterestTransactions(list);

        verify(transactionRepository, times(1)).saveMonthlyInterestTransactions(any());
    }

    @Test
    public void allAccountTransactionsThenInvokeTransactionRepo1TimeAndCheckConvertEntityToDto() {
        UUID uuid = UUID.randomUUID();
        Period period = new Period(LocalDate.of(2023, 3, 14), LocalDate.of(2023, 4, 14));

        TransactionEntity entity = TransactionEntity.builder()
                .setDate(LocalDateTime.of(2023, 3, 25, 14, 32))
                .setType("MONEY_TRANSFER")
                .setSum(1500)
                .build();

        TransactionEntity entity2 = TransactionEntity.builder()
                .setDate(LocalDateTime.of(2023, 4, 2, 23, 10))
                .setType("WAGE")
                .setSum(854)
                .build();

        List<TransactionEntity> listFromDB = new ArrayList<>(List.of(entity, entity2));

        Transaction.TransactionBuilder builder = Transaction.builder();

        List<Transaction> expected = listFromDB.stream()
                .map(trEnt -> builder
                        .setDate(trEnt.getDate())
                        .setType(TransactionType.valueOf(trEnt.getType()))
                        .setSum(trEnt.getSum())
                        .build())
                .toList();

        when(transactionRepository.allAccountTransactions(uuid, period)).thenReturn(listFromDB);

        List<Transaction> transactions = transactionService.allAccountTransactions(uuid, period);

        assertEquals(expected, transactions);
        verify(transactionRepository, times(1)).allAccountTransactions(uuid, period);
    }

    @Test
    public void getSumInfoAboutTransactionsThenInvokeTransactionRepo1Time() {
        UUID uuid = UUID.randomUUID();
        Period period = new Period(LocalDate.of(2023, 3, 14), LocalDate.of(2023, 4, 14));

        transactionService.getSumInfoAboutTransactions(uuid, period);

        verify(transactionRepository, times(1)).getSumInfoAboutTransactions(uuid, period);
    }
}
