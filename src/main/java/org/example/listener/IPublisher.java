package org.example.listener;

public interface IPublisher<T> {
    void notify(T event);
}
