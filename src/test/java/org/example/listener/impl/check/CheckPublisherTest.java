package org.example.listener.impl.check;

import org.example.core.dto.docs.Check;
import org.example.core.events.CheckEvent;
import org.example.listener.api.IListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckPublisherTest {
    private CheckPublisher publisher;
    @Mock
    private IListener<CheckEvent> listener;
    private CheckEvent event;
    @BeforeEach
    public void setUp() {
        this.publisher = new CheckPublisher(listener);
        this.event = new CheckEvent(new Check());
    }

    @Test
    public void notifyCheckEvent() {
        publisher.notify(event);
        verify(listener, times(1)).handleEvent(any(CheckEvent.class));
    }

    @Test
    public void notifyCheckEvents() {
        publisher.notify(List.of(event));
        verify(listener, times(1)).handleEvents(anyList());
    }
}