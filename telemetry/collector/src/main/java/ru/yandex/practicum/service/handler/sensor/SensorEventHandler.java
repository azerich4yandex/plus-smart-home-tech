package ru.yandex.practicum.service.handler.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorEventHandler {

    void handleEvent(SensorEventProto eventProto);

    SensorEventProto.PayloadCase getMessageSensorType();
}
