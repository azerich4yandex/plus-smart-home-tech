package ru.yandex.practicum.service.handler.sensor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.service.producer.CollectorKafkaProducer;

@Slf4j
@Component
public class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorAvro> {

    public TemperatureSensorEventHandler(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    protected TemperatureSensorAvro protoToAvro(SensorEventProto eventProto) {
        log.info("Преобразование SensorEventProto в TemperatureSensorAvro");
        TemperatureSensorProto proto = eventProto.getTemperatureSensorEvent();
        TemperatureSensorAvro avro = TemperatureSensorAvro.newBuilder()
                .setTemperatureC(proto.getTemperatureC())
                .setTemperatureF(proto.getTemperatureF())
                .build();

        log.info("Преобразование SensorEventProto в TemperatureSensorAvro завершено");
        return avro;
    }

    @Override
    public SensorEventProto.PayloadCase getMessageSensorType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }
}
