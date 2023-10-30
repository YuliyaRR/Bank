package org.example.listener.impl.check;

import org.example.core.dto.docs.Check;
import org.example.core.events.CheckEvent;
import org.example.service.api.IDocCreationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CheckListenerTest {
    private CheckListener checkListener;
    @Mock
    private IDocCreationService docCreationService;
    private CheckEvent checkEvent;

    @BeforeEach
    public void setUp() {
        this.checkListener = new CheckListener(docCreationService);
        this.checkEvent = new CheckEvent(new Check());
    }

    @Test
    public void handleEvent() {
        checkListener.handleEvent(checkEvent);
        verify(docCreationService, times(1)).createCheck(any(Check.class));
    }
}