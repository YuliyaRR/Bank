package org.example.core.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.core.dto.Transaction;
@Getter
@RequiredArgsConstructor
public class TransactionEvent {
    private final Transaction transaction;
}

