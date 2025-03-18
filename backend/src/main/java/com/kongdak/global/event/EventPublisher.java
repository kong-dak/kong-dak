package com.kongdak.global.event;

public interface EventPublisher {
    void publish(DomainEvent event);
}
