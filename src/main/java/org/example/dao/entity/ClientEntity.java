package org.example.dao.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ClientEntity {
    private UUID id;
    private String name;
    private Set<BankEntity> banks;
}
