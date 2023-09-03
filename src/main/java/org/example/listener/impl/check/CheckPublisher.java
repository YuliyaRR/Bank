package org.example.listener.impl.check;

import org.example.core.events.CheckEvent;
import org.example.listener.api.IListener;
import org.example.listener.api.IPublisher;

import java.util.List;


public class CheckPublisher implements IPublisher<CheckEvent> {
    private final IListener<CheckEvent> listener;

    public CheckPublisher() {
       listener = new CheckListener();
    }

    @Override
    public void notify(CheckEvent event) {
        listener.handleEvent(event);
    }

    @Override
    public void notify(List<CheckEvent> events) {
        listener.handleEvents(events);
    }
}