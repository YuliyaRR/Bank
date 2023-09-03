package org.example.listener.api;

import java.util.List;

public interface IListener<T> {
    void handleEvent(T event);
    void handleEvents(List<T> events);
}
