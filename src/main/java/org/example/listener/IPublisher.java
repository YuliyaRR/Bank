package org.example.listener;

import java.util.List;

public interface IPublisher<T> {
    void notify(T event);
    void notify(List<T> events);
}
