package ru.yandex.practicum.service.handler.hub;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler {

    void handleEvent(HubEventAvro event);

    String getEventType();
}
