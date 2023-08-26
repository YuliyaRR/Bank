package org.example.dao.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.core.dto.Currency;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class CurrencyEntity {
    private String name;

    public CurrencyEntity(Currency currency) {
        this.name = currency.name();
    }
}
