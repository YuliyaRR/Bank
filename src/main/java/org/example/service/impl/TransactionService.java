package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.core.dto.Period;
import org.example.core.dto.SumTransactionsInfo;
import org.example.core.dto.Transaction;
import org.example.core.dto.TransactionType;
import org.example.dao.repositories.api.ITransactionRepository;
import org.example.dao.entity.TransactionEntity;
import org.example.service.api.ITransactionService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    private final ITransactionRepository transactionRepository;

    /** Метод передает транзакцию для сохранения в БД
     * @param transaction транзакция, которую необходимо сохранить
     */
    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.saveTransaction(convertToEntity(transaction));
    }

    /** Метод передает список транзакций по начислению ежемесячных процентов в БД для сохранения
     * @param transactions список транзакций по начислению ежемесячных процентов
     */
    @Override
    public void saveMonthlyInterestTransactions(List<Transaction> transactions) {
        transactionRepository.saveMonthlyInterestTransactions(
                transactions.stream()
                .map(this::convertToEntity)
                .toList()
        );
    }

    /** Метод предоставляет список всех транзакций по счету
     * @param account счет, по которому нужно найти транзакции
     * @param period период, за который ябудет сформирован список транзакций
     * @return список всех транзакций по счету. Если транзакций по счету нет, то возвращается пустой список
     */
    @Override
    public List<Transaction> allAccountTransactions(UUID account, Period period) {
        List<TransactionEntity> entities = transactionRepository.allAccountTransactions(account, period);

        return entities.stream()
                .map(this::convertToDto)
                .toList();
    }


    /** Метод предоставляет информацию о сумме приходно-расходных операций за заданный период
     * @param account счет, по которому запрашивается информация
     * @param period период, за который запрашивается информация
     * @return SumTransactionsInfo - объект, содержащий итоговую информация о приходно-расходных операциях за период
     */
    @Override
    public SumTransactionsInfo getSumInfoAboutTransactions(UUID account, Period period) {
        return transactionRepository.getSumInfoAboutTransactions(account, period);
    }

    /** Метод конвертирует dto Transaction в TransactionEntity
     * @param transaction объект-источник
     * @return TransactionEntity
     */
    private TransactionEntity convertToEntity(Transaction transaction) {
        return TransactionEntity.builder()
                .setId(transaction.getId())
                .setCurrency(transaction.getCurrency().name())
                .setDate(transaction.getDate())
                .setAccountFrom(transaction.getAccountFrom())
                .setAccountTo(transaction.getAccountTo())
                .setSum(transaction.getSum())
                .setType(transaction.getType().name())
                .build();
    }

    /** Метод конвертирует TransactionEntity в Transaction
     * @param entity объект-источник
     * @return Transaction
     */
    private Transaction convertToDto(TransactionEntity entity) {
        return Transaction.builder()
                .setDate(entity.getDate())
                .setType(TransactionType.valueOf(entity.getType()))
                .setSum(entity.getSum())
                .build();
    }
}
