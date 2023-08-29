package org.example.listener;

public interface IListener<T> {
    void handleEvent(T event);
}
