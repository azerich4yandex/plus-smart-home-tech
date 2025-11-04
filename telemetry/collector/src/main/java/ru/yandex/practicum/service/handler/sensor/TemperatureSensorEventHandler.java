package ru.yandex.practicum.service.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto.PayloadCase;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.mapper.SensorEventAvroMapper;
import ru.yandex.practicum.mapper.SensorEventProtoMapper;
import ru.yandex.practicum.model.sensor.SensorEvent;

@Component
public class TemperatureSensorEventHandler extends BaseSensorEventHandler {

    public TemperatureSensorEventHandler(
            KafkaTemplate<String, SpecificRecordBase> kafkaTemplate) {
        super(kafkaTemplate);
    }

    @Override
    protected SensorEvent mapSensorEventProtoToModel(SensorEventProto eventProto) {
        SensorEvent event = SensorEventProtoMapper.toTemperatureSensorEvent(eventProto);
        return mapBaseProtoFieldsToModel(event, eventProto);
    }

    @Override
    protected SensorEventAvro mapModelToSensorEventAvro(SensorEvent event) {
        SensorEventAvro avro = SensorEventAvroMapper.toAvro(event);
        return mapBaseModelFieldsToAvro(event, avro);
    }

    @Override
    public PayloadCase getMessageSensorType() {
        return PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }
}
