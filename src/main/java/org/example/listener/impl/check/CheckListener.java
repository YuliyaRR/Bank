package org.example.listener.impl.check;

import org.example.core.events.CheckEvent;
import org.example.listener.api.IListener;
import org.example.service.api.IDocCreationService;
import org.example.service.factory.DocCreationServiceSingleton;

import java.beans.PropertyVetoException;
import java.util.List;


public class CheckListener implements IListener<CheckEvent> {
    private final IDocCreationService docCreationService;

    public CheckListener() {
        try {
            this.docCreationService = DocCreationServiceSingleton.getInstance();
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleEvent(CheckEvent event) {
        docCreationService.createCheck(event.getCheck());
    }

    @Override
    public void handleEvents(List<CheckEvent> events) {}
}
