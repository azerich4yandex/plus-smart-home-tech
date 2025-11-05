package ru.yandex.practicum.service.handler.hub;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubEventHandler {

    void handleEvent(HubEventProto eventProto);

    HubEventProto.PayloadCase getMessageHubType();
}
