package org.example.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.example.core.dto.*;
import org.example.service.api.IDocCreationService;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DocCreationService implements IDocCreationService {
    private final Properties properties;
    private final static String PATH_FOR_SAVING_CHECK = System.getProperty("PATH_FOR_SAVING_CHECK");
    private final static String PATH_FOR_SAVING_ACCOUNT_STATEMENT =  System.getProperty("PATH_FOR_SAVING_ACCOUNT_STATEMENT");
    private final static String BANK_NAME = "BANK_NAME";

    /**Метод передает объект Check на формирование pdf-документа
     * @param check объект-источник информации
     */
    @Override
    public void createCheck(Check check) {
        createPDFCheck(check);
    }

    /**Метод передает объект AccountStatement на формирование pdf-документа
     * @param accountStatement объект-источник информации
     */
    @Override
    public void createAccountStatement(AccountStatement accountStatement) {
       createPDFAccountStatement(accountStatement);
    }

    /** Метод создает pdf-документ Check
     * @param check объект-источник информации
     */
    private void createPDFCheck(Check check) {
        try {
            UUID number = check.getNumber();
            Document document = new Document();
            PdfWriter.getInstance(document,
                    new FileOutputStream(String.format("%s/%s.pdf",PATH_FOR_SAVING_CHECK, number.toString())));

            document.open();

            Paragraph paragraph = new Paragraph("Bank check");
            paragraph.setAlignment(Element.ALIGN_CENTER);

            document.add(paragraph);

            PdfPTable table = new PdfPTable(2);
            table.setSpacingBefore(20f);
            table.setWidths(new float[] {30f, 70f});

            fillTableCheck(table, check);

            document.add(table);
            document.close();

        } catch (Exception e) {
            throw new RuntimeException(e);//TODO custom exception
        }
    }

    /**
     * Метод заполняет таблицу документа Check
     * @param table pdf-таблица, которая будет заполнена данными
     * @param check - объект-источник информации
     */
    private void fillTableCheck(PdfPTable table, Check check) {
        LocalDateTime localDateTime = check.getLocalDateTime();

        fill2CellInRow(table, "Check:", check.getNumber().toString());
        fill2CellInRow(table, localDateTime.toLocalDate().toString(), localDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        fill2CellInRow(table, "Transaction type:", check.getTransactionType().getName());
        fill2CellInRow(table, "Sender's bank:", check.getBankFrom().getName());
        fill2CellInRow(table, "Recipient's bank:", check.getBankTo().getName());
        fill2CellInRow(table, "Sender's account:", fillAccountCell(check.getAccountFrom()));
        fill2CellInRow(table, "Recipient account:", fillAccountCell(check.getAccountTo()));
        fill2CellInRow(table, "Sum:", String.format("%.2f %s", check.getSum(), check.getCurrency().name()));
    }

    /**
     * Метод создает pdf-документ AccountStatement
     * @param accountStatement объект-источник информации
     */
    private void createPDFAccountStatement (AccountStatement accountStatement) {
        UUID uuidAccount = accountStatement.getAccount().getNum();
        Period period = accountStatement.getPeriod();

        Document document = new Document();
        try {
            PdfWriter.getInstance(document,
                    new FileOutputStream(
                            String.format("%s/%s for %sT%s.pdf",
                                    PATH_FOR_SAVING_ACCOUNT_STATEMENT, uuidAccount, period,
                                    accountStatement.getCreationTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH-mm-ss")))));

            document.open();

            Paragraph paragraph = new Paragraph("Account statement");
            paragraph.setAlignment(Element.ALIGN_CENTER);

            document.add(paragraph);

            paragraph = new Paragraph(properties.getProperty(BANK_NAME));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

            PdfPTable table = new PdfPTable(2);
            table.setSpacingBefore(20f);
            table.setWidths(new float[] {35f, 65f});

            fillHeaderAccountStatement(table, accountStatement);
            document.add(table);

            table = new PdfPTable(3);
            table.setSpacingBefore(10f);
            table.setWidths(new float[] {20f, 50f, 30f});

            fillBodyAccountStatement(table, accountStatement);
            document.add(table);

            document.close();

        } catch (Exception e) {
            throw new RuntimeException(e);//TODO custom exception
        }

    }

    /**
     * Метод заполняет ряд таблицы, состоящей из двух столбцов.
     * Во втором столбце выравнивает данные по правому краю.
     * @param table pdf-таблица, которая будет заполнена данными
     * @param key  наименование строки таблицы
     * @param value значение, соответствующее ключу
     */
    private void fill2CellInRow(PdfPTable table, String key, String value) {
        table.addCell(key);
        PdfPCell cell = new PdfPCell(new Phrase(value));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
    }

    /**
     * Метод заполняет ячейку таблицы, содержащую данные о счете
     * @param account счет, который необходимо вставить в таблицу
     * @return String строковое представление счета
     */
    private String fillAccountCell(UUID account) {
        return account == null ? "" : account.toString();
    }

    /**
     * Метод заполняет верхнуюю часть(шапку) выписки по счету.
     * @param table pdf-таблица, которая будет заполнена данными
     * @param accountStatement объект-источник информации
     */
    private void fillHeaderAccountStatement(PdfPTable table, AccountStatement accountStatement) {
        Account account = accountStatement.getAccount();
        LocalDateTime creationTime = accountStatement.getCreationTime();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH.mm");

        fill2CellInRow(table, "Client", account.getOwner().getName());
        fill2CellInRow(table, "Account", account.getNum().toString());
        fill2CellInRow(table, "Currency", account.getCurrency().name());
        fill2CellInRow(table, "Opening date", account.getDateOpen().format(dateFormatter));
        fill2CellInRow(table, "Period", accountStatement.getPeriod().toString());
        fill2CellInRow(table, "Date and time of creation", String.format("%s, %s",
                creationTime.toLocalDate().format(dateFormatter), creationTime.toLocalTime().format(timeFormatter)));
        fill2CellInRow(table, "Balance", String.format("%.2f %s", account.getBalance(), account.getCurrency().name()));
    }

    /**
     * Метод заполняет основную часть выписки по счету, содержащую информацию о движении по счету
     * @param table pdf-таблица, которая будет заполнена данными
     * @param accountStatement объект-источник информации
     */
    private void fillBodyAccountStatement(PdfPTable table, AccountStatement accountStatement) {
        List<Transaction> transactions = accountStatement.getTransactions();
        String currency = accountStatement.getAccount().getCurrency().name();

        Stream.of("Date", "Transaction type", "Sum")
                        .forEach(columnTitle -> {
                            PdfPCell header = new PdfPCell(new Phrase(columnTitle));
                            header.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(header);
                        });

        if(transactions.isEmpty()) {
            return;
        }

        transactions.stream()
                        .forEach(transaction -> {
                            fillRowByTransaction(table, transaction, currency);
                        });
    }

    /**
     * Метод заполняет ряд выписки по счету (раздел - движение по счету) данными об операции
     * @param table pdf-таблица, которая будет заполнена данными
     * @param transaction объект-источник информации о транзакции
     * @param currency валюта операции
     */
    private void fillRowByTransaction(PdfPTable table, Transaction transaction, String currency){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        table.addCell(transaction.getDate().toLocalDate().format(dateFormatter));
        table.addCell(transaction.getType().getName());
        table.addCell(String.format("%.2f %s", transaction.getSum(), currency));
    }

}
