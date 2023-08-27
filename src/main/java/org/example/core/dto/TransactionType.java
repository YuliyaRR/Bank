package org.example.core.dto;

public enum TransactionType {
    WAGE,//client <- company
    MONEY_TRANSFER, //client <-> client
    DEPOSIT_INTEREST, //client <- bank (equals client <- company)
    WITHDRAWALS, //client -> cash
    PAYMENT_FOR_SERVICES, //client -> company
    CASH_REPLENISHMENT //client <- cash
}
