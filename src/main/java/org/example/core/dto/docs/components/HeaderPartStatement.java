package org.example.core.dto.docs.components;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.example.core.dto.Account;
import org.example.core.dto.Period;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class HeaderPartStatement {
    private Account account;
    private Period period;
    private LocalDateTime creationTime;
}
