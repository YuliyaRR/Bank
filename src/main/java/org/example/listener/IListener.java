package org.example.listener;

import java.util.List;

public interface IListener<T> {
    void handleEvent(T event);
    void handleEvents(List<T> events);
}
