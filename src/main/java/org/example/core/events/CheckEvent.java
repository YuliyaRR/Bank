package org.example.core.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.core.dto.Check;

@Getter
@RequiredArgsConstructor
public class CheckEvent {
    private final Check check;
}

