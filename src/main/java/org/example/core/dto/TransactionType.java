package org.example.core.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransactionType {
    WAGE("Wage"),//client <- company
    MONEY_TRANSFER("Money transfer"), //client <-> client
    DEPOSIT_INTEREST("Deposit interest"), //client <- bank (equals client <- company)
    WITHDRAWALS("Cash withdrawal"), //client -> cash
    PAYMENT_FOR_SERVICES ("Payment for services"), //client -> company
    CASH_REPLENISHMENT("Cash replenishment"); //client <- cash

    private final String name;

    @JsonValue
    public String getName() {
        return name;
    }
}
