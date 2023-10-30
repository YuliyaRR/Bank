package org.example.service.impl;

import org.example.core.dto.*;
import org.example.core.dto.docs.AccountStatement;
import org.example.core.dto.docs.Check;
import org.example.core.dto.docs.MoneyStatement;
import org.example.service.api.IDocCreationService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DocCreationServiceTest {
    @Mock
    private Properties properties;
    private IDocCreationService docCreationService;
    private static final String TEST_FOLDER = "test_folder";
    private static final String NAME_PROPERTY_FOR_SAVING_CHECK = "PATH_FOR_SAVING_CHECK";
    private static final String NAME_PROPERTY_FOR_SAVING_ACCOUNT_STATEMENT = "PATH_FOR_SAVING_ACCOUNT_STATEMENT";
    private static final String NAME_PROPERTY_FOR_SAVING_MONEY_STATEMENT = "PATH_FOR_SAVING_MONEY_STATEMENT";
    private static final String NAME_FOLDER_FOR_SAVING_CHECK = "/check";
    private static final String NAME_FOLDER_FOR_SAVING_ACCOUNT_STATEMENT = "/statement";
    private static final String NAME_FOLDER_FOR_SAVING_MONEY_STATEMENT = "/statement-money";
    private final static String BANK_NAME = "Clever-Bank";
    private static String pathForSavingCheck;
    private static String pathForSavingAccountStatement;
    private static String pathForSavingMoneyStatement;
    private static File folder;

    @BeforeAll
    public static void initFolder() {
        folder = new File(TEST_FOLDER);
        folder.mkdir();
        pathForSavingCheck = String.format("%s%s", TEST_FOLDER, NAME_FOLDER_FOR_SAVING_CHECK);
        pathForSavingAccountStatement = String.format("%s%s", TEST_FOLDER, NAME_FOLDER_FOR_SAVING_ACCOUNT_STATEMENT);
        pathForSavingMoneyStatement = String.format("%s%s", TEST_FOLDER, NAME_FOLDER_FOR_SAVING_MONEY_STATEMENT);
        System.setProperty(NAME_PROPERTY_FOR_SAVING_CHECK, pathForSavingCheck);
        System.setProperty(NAME_PROPERTY_FOR_SAVING_ACCOUNT_STATEMENT, pathForSavingAccountStatement);
        System.setProperty(NAME_PROPERTY_FOR_SAVING_MONEY_STATEMENT, pathForSavingMoneyStatement);
    }


    @BeforeEach
    public void setUp() {
        this.docCreationService = new DocCreationService(properties);

        if (folder.listFiles().length != 0) {
            tearDown();
        }

    }

    @AfterEach
    public void tearDown() {
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory()) {
                for (File innFile : Objects.requireNonNull(file.listFiles())) {
                    innFile.delete();
                }
                file.delete();
            }
        }
    }

    @AfterAll
    public static void deleteFolder() {
        folder.delete();
        System.clearProperty(NAME_PROPERTY_FOR_SAVING_CHECK);
        System.clearProperty(NAME_PROPERTY_FOR_SAVING_ACCOUNT_STATEMENT);
        System.clearProperty(NAME_PROPERTY_FOR_SAVING_MONEY_STATEMENT);
    }

    @Test
    public void createCheckPDFThenCheckItInFolder() {
        File folderCheck = new File(pathForSavingCheck);
        folderCheck.mkdir();

        assertEquals(0, Objects.requireNonNull(folderCheck.listFiles()).length);

        Check check = Check.builder()
                .setNumber(UUID.randomUUID())
                .setLocalDateTime(LocalDateTime.now())
                .setTransactionType(TransactionType.WAGE)
                .setBankFrom(new Bank("Test Bank From"))
                .setBankTo(new Bank("Test Bank To"))
                .setAccountFrom(UUID.randomUUID())
                .setAccountTo(UUID.randomUUID())
                .setSum(1000)
                .setCurrency(Currency.RUB)
                .build();

        docCreationService.createCheck(check);

        File[] files = folderCheck.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
        assertEquals(1, Objects.requireNonNull(files).length);
    }

    @Test
    public void createCheckPDFWhenFolderForCheckIsNotCreatedThenThrowException() {
        Check check = Check.builder()
                .setNumber(UUID.randomUUID())
                .setLocalDateTime(LocalDateTime.now())
                .setTransactionType(TransactionType.WAGE)
                .setBankFrom(new Bank("Test Bank From"))
                .setBankTo(new Bank("Test Bank To"))
                .setAccountFrom(UUID.randomUUID())
                .setAccountTo(UUID.randomUUID())
                .setSum(1000)
                .setCurrency(Currency.RUB)
                .build();

        assertThrows(RuntimeException.class, () -> docCreationService.createCheck(check));
    }

    @Test
    public void createAccountStatementPDFThenCheckItInFolder() {
        File folderAccountStatement = new File(pathForSavingAccountStatement);
        folderAccountStatement.mkdir();

        assertEquals(0, Objects.requireNonNull(folderAccountStatement.listFiles()).length);

        AccountStatement accountStatement = AccountStatement.builder()
                .setAccount(Account.builder()
                        .setNum(UUID.randomUUID())
                        .setCurrency(Currency.RUB)
                        .setBank(new Bank(BANK_NAME))
                        .setDateOpen(LocalDate.of(2022, 10, 15))
                        .setDateLastTransaction(LocalDateTime.of(2023, 10, 16, 14, 55))
                        .setBalance(150.26)
                        .setOwner(new Client(UUID.randomUUID(), "Test Client 1"))
                        .build())
                .setPeriod(new Period(LocalDate.of(2023, 2, 15), LocalDate.of(2023, 9, 11)))
                .setCreationTime(LocalDateTime.now())
                .setTransaction(Transaction.builder()
                        .setId(UUID.randomUUID())
                        .setDate(LocalDateTime.of(2023, 10, 16, 14, 55))
                        .setAccountTo(UUID.randomUUID())
                        .setAccountFrom(UUID.randomUUID())
                        .setCurrency(Currency.RUB)
                        .setSum(100)
                        .setType(TransactionType.PAYMENT_FOR_SERVICES)
                        .build())
                .build();

        docCreationService.createAccountStatement(accountStatement);

        File[] files = folderAccountStatement.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
        assertEquals(1, Objects.requireNonNull(files).length);
    }

    @Test
    public void createAccountStatementPDFWhenFolderForCheckIsNotCreatedThenThrowException() {
        AccountStatement accountStatement = AccountStatement.builder()
                .setAccount(Account.builder()
                        .setNum(UUID.randomUUID())
                        .setCurrency(Currency.RUB)
                        .setBank(new Bank(BANK_NAME))
                        .setDateOpen(LocalDate.of(2022, 10, 15))
                        .setDateLastTransaction(LocalDateTime.of(2023, 10, 16, 14, 55))
                        .setBalance(150.26)
                        .setOwner(new Client(UUID.randomUUID(), "Test Client 1"))
                        .build())
                .setPeriod(new Period(LocalDate.of(2023, 2, 15), LocalDate.of(2023, 9, 11)))
                .setCreationTime(LocalDateTime.now())
                .setTransaction(Transaction.builder()
                        .setId(UUID.randomUUID())
                        .setDate(LocalDateTime.of(2023, 10, 16, 14, 55))
                        .setAccountTo(UUID.randomUUID())
                        .setAccountFrom(UUID.randomUUID())
                        .setCurrency(Currency.RUB)
                        .setSum(100)
                        .setType(TransactionType.PAYMENT_FOR_SERVICES)
                        .build())
                .build();

        assertThrows(RuntimeException.class, () -> docCreationService.createAccountStatement(accountStatement));

    }
    @Test
    public void createMoneyStatementPDFThenCheckItInFolder() {
        File folderMoneyStatement = new File(pathForSavingMoneyStatement);
        folderMoneyStatement.mkdir();

        assertEquals(0, Objects.requireNonNull(folderMoneyStatement.listFiles()).length);

        MoneyStatement moneyStatement = MoneyStatement.builder()
                .setAccount(Account.builder()
                        .setNum(UUID.randomUUID())
                        .setCurrency(Currency.RUB)
                        .setBank(new Bank(BANK_NAME))
                        .setDateOpen(LocalDate.of(2022, 10, 15))
                        .setDateLastTransaction(LocalDateTime.of(2023, 10, 16, 14, 55))
                        .setBalance(150.26)
                        .setOwner(new Client(UUID.randomUUID(), "Test Client 1"))
                        .build())
                .setPeriod(new Period(LocalDate.of(2023, 2, 15), LocalDate.of(2023, 9, 11)))
                .setCreationTime(LocalDateTime.now())
                .setSumTransactionsInfo(new SumTransactionsInfo(350, 200))
                .build();

        docCreationService.createMoneyStatement(moneyStatement);

        File[] files = folderMoneyStatement.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
        assertEquals(1, Objects.requireNonNull(files).length);
    }

    @Test
    public void createMoneyStatementPDFWhenFolderForCheckIsNotCreatedThenThrowException() {
        MoneyStatement moneyStatement = MoneyStatement.builder()
                .setAccount(Account.builder()
                        .setNum(UUID.randomUUID())
                        .setCurrency(Currency.RUB)
                        .setBank(new Bank(BANK_NAME))
                        .setDateOpen(LocalDate.of(2022, 10, 15))
                        .setDateLastTransaction(LocalDateTime.of(2023, 10, 16, 14, 55))
                        .setBalance(150.26)
                        .setOwner(new Client(UUID.randomUUID(), "Test Client 1"))
                        .build())
                .setPeriod(new Period(LocalDate.of(2023, 2, 15), LocalDate.of(2023, 9, 11)))
                .setCreationTime(LocalDateTime.now())
                .setSumTransactionsInfo(new SumTransactionsInfo(350, 200))
                .build();

        assertThrows(RuntimeException.class, () -> docCreationService.createMoneyStatement(moneyStatement));
    }

}