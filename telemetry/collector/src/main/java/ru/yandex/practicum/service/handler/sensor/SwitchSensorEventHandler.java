package ru.yandex.practicum.service.handler.sensor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.service.producer.CollectorKafkaProducer;

@Slf4j
@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> {

    public SwitchSensorEventHandler(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    protected SwitchSensorAvro protoToAvro(SensorEventProto eventProto) {
        log.info("Преобразование SensorEventProto в SwitchSensorAvro");

        SwitchSensorProto proto = eventProto.getSwitchSensorEvent();
        SwitchSensorAvro avro = SwitchSensorAvro.newBuilder()
                .setState(proto.getState())
                .build();

        log.info("Преобразование SensorEventProto в SwitchSensorAvro завершено");
        return avro;
    }

    @Override
    public SensorEventProto.PayloadCase getMessageSensorType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }
}
