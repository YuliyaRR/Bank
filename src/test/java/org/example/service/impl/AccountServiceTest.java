package org.example.service.impl;

import org.example.core.dto.Check;
import org.example.core.dto.Currency;
import org.example.core.dto.Transaction;
import org.example.core.dto.TransactionType;
import org.example.core.events.CheckEvent;
import org.example.dao.entity.AccountEntity;
import org.example.dao.repositories.api.IAccountRepository;
import org.example.listener.impl.check.CheckPublisher;
import org.example.service.api.IAccountService;
import org.example.service.api.IBankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    private final static UUID NUM_ACC_TO = UUID.fromString("12605bea-4688-4a8a-b64f-d29e24eb6d81");
    private final static UUID NUM_ACC_FROM = UUID.fromString("22605bea-4688-4a8a-b64f-d29e24eb6d81");
    private final static String EXCEPTION_WRONG_TYPE = "Transaction type doesn't match the operation being performed";
    private final static String EXCEPTION_WRONG_CURRENCY = "Payment currency doesn't match with account currency";
    private final static String EXCEPTION_WRONG_SUM = "The amount of any transaction must be greater than zero";
    private final static String EXCEPTION_BALANCE = "You don't have enough money for this operation";
    @Mock
    private IAccountRepository accountRepository;
    @Mock
    private IBankService bankService;
    @Mock
    private CheckPublisher publisher;
    private IAccountService accountService;
    private Transaction in;

    @BeforeEach
    public void setUp() {
       this.accountService = new AccountService(accountRepository,bankService, publisher);
       this.in = Transaction.builder()
               .setAccountTo(NUM_ACC_TO)
               .setAccountFrom(NUM_ACC_FROM)
               .setCurrency(Currency.USD)
               .setSum(100)
               .build();
    }

    @Test
    public void addMoneyWhenAllDataIsValid() {
        in.setAccountFrom(null);
        in.setType(TransactionType.CASH_REPLENISHMENT);

        AccountEntity fromDao = AccountEntity.builder().setBalance(1000).setCurrency("USD").build();

        when(accountRepository.checkAccount(any(UUID.class))).thenReturn(fromDao);
        when(accountRepository.updateBalanceCashOperation(any(Transaction.class))).thenAnswer((Answer<Transaction>) invocation -> (Transaction) invocation.getArgument(0));

        Check check = accountService.addMoney(in);

        Mockito.verify(accountRepository, Mockito.times(1)).checkAccount(Mockito.any());
        Mockito.verify(accountRepository, Mockito.times(1)).updateBalanceCashOperation(Mockito.any());
        Mockito.verify(bankService, Mockito.times(1)).getBankByAccount(Mockito.any());
        Mockito.verify(publisher, Mockito.times(1)).notify(Mockito.any(CheckEvent.class));

        assertNotNull(check);
        assertNotNull(check.getNumber());
        assertEquals(NUM_ACC_TO, check.getAccountTo());
        assertEquals(in.getType(), check.getTransactionType());
        assertEquals(in.getCurrency(), check.getCurrency());
        assertEquals(in.getSum(), check.getSum());
        assertNull(check.getAccountFrom());
    }
    @Test
    public void addMoneyWhenTransactionTypeWrongThenThrowException() {
        in.setAccountFrom(null);
        in.setType(TransactionType.WAGE);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.addMoney(in));
        assertEquals(EXCEPTION_WRONG_TYPE, exception.getMessage());
    }

    @Test
    public void addMoneyWhenTransactionSumWrongThenThrowException(){
        in.setAccountFrom(null);
        in.setType(TransactionType.CASH_REPLENISHMENT);
        in.setSum(-100);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.addMoney(in));
        assertEquals(EXCEPTION_WRONG_SUM, exception.getMessage());
    }

    @Test
    public void addMoneyWhenAccountToWasNotFoundThenThrowException(){
        in.setAccountFrom(null);
        in.setType(TransactionType.CASH_REPLENISHMENT);

        when(accountRepository.checkAccount(any(UUID.class))).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> accountService.addMoney(in));
        Mockito.verify(accountRepository, Mockito.times(1)).checkAccount(Mockito.any());
    }

    @Test
    public void addMoneyWhenCurrencyWrongThenThrowException() {
        in.setAccountFrom(null);
        in.setType(TransactionType.CASH_REPLENISHMENT);
        in.setCurrency(Currency.EUR);

        AccountEntity fromDao = AccountEntity.builder().setBalance(1000).setCurrency("USD").build();

        when(accountRepository.checkAccount(any(UUID.class))).thenReturn(fromDao);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.addMoney(in));
        assertEquals(EXCEPTION_WRONG_CURRENCY, exception.getMessage());
    }

    @Test
    void withdrawalMoneyWhenAllDataIsValid() {
        in.setAccountTo(null);
        in.setType(TransactionType.WITHDRAWALS);

        AccountEntity fromDao = AccountEntity.builder().setBalance(1000).setCurrency("USD").build();

        when(accountRepository.checkAccount(any(UUID.class))).thenReturn(fromDao);
        when(accountRepository.updateBalanceCashOperation(in)).thenAnswer((Answer<Transaction>) invocation -> (Transaction) invocation.getArgument(0));

        Check check = accountService.withdrawalMoney(in);

        Mockito.verify(accountRepository, Mockito.times(1)).checkAccount(Mockito.any());
        Mockito.verify(accountRepository, Mockito.times(1)).updateBalanceCashOperation(Mockito.any());
        Mockito.verify(bankService, Mockito.times(1)).getBankByAccount(Mockito.any());
        Mockito.verify(publisher, Mockito.times(1)).notify(Mockito.any(CheckEvent.class));

        assertNotNull(check);
        assertNotNull(check.getNumber());
        assertEquals(NUM_ACC_FROM, check.getAccountFrom());
        assertEquals(in.getType(), check.getTransactionType());
        assertEquals(in.getCurrency(), check.getCurrency());
        assertEquals(Math.abs(in.getSum()), check.getSum());
        assertNull(check.getAccountTo());
    }

    @Test
    public void withdrawalMoneyWhenTransactionTypeWrongThenThrowException() {
        in.setAccountTo(null);
        in.setType(TransactionType.WAGE);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.withdrawalMoney(in));
        assertEquals(EXCEPTION_WRONG_TYPE, exception.getMessage());
    }

    @Test
    public void withdrawalMoneyWhenTransactionSumWrongThenThrowException(){
        in.setAccountTo(null);
        in.setType(TransactionType.WITHDRAWALS);
        in.setSum(-100);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.withdrawalMoney(in));
        assertEquals(EXCEPTION_WRONG_SUM, exception.getMessage());
    }

    @Test
    public void withdrawalMoneyWhenAccountToWasNotFoundThenThrowException(){
        in.setAccountTo(null);
        in.setType(TransactionType.WITHDRAWALS);

        when(accountRepository.checkAccount(any(UUID.class))).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> accountService.withdrawalMoney(in));
        Mockito.verify(accountRepository, Mockito.times(1)).checkAccount(Mockito.any());
    }

    @Test
    public void withdrawalMoneyWhenCurrencyWrongThenThrowException() {
        in.setAccountTo(null);
        in.setType(TransactionType.WITHDRAWALS);
        in.setCurrency(Currency.EUR);

        AccountEntity fromDao = AccountEntity.builder().setBalance(1000).setCurrency("USD").build();

        when(accountRepository.checkAccount(any(UUID.class))).thenReturn(fromDao);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.withdrawalMoney(in));
        assertEquals(EXCEPTION_WRONG_CURRENCY, exception.getMessage());
    }

    @Test
    public void withdrawalMoneyWhenInsufficientBalanceThenThrowException() {
        in.setAccountTo(null);
        in.setType(TransactionType.WITHDRAWALS);

        AccountEntity fromDao = AccountEntity.builder().setBalance(50).setCurrency("USD").build();

        when(accountRepository.checkAccount(any(UUID.class))).thenReturn(fromDao);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.withdrawalMoney(in));
        assertEquals(EXCEPTION_BALANCE, exception.getMessage());
    }
}