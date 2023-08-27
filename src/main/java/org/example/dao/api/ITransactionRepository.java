package org.example.dao.api;

import org.example.dao.entity.TransactionEntity;

public interface ITransactionRepository {
    void save(TransactionEntity entity);
}
