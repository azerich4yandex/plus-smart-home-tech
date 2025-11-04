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
public class ClimateSensorEventHandler extends BaseSensorEventHandler {

    public ClimateSensorEventHandler(
            KafkaTemplate<String, SpecificRecordBase> kafkaTemplate) {
        super(kafkaTemplate);
    }

    @Override
    protected SensorEvent mapSensorEventProtoToModel(SensorEventProto eventProto) {
        SensorEvent event = SensorEventProtoMapper.toClimateSensorEvent(eventProto);
        return mapBaseProtoFieldsToModel(event, eventProto);

    }

    @Override
    protected SensorEventAvro mapModelToSensorEventAvro(SensorEvent event) {
        SensorEventAvro eventAvro = SensorEventAvroMapper.toAvro(event);
        return mapBaseModelFieldsToAvro(event, eventAvro);
    }

    @Override
    public PayloadCase getMessageSensorType() {
        return PayloadCase.CLIMATE_SENSOR_EVENT;
    }
}
