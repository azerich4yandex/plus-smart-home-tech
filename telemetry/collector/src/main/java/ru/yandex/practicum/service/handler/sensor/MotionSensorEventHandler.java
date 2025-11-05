package ru.yandex.practicum.service.handler.sensor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.service.producer.CollectorKafkaProducer;

@Slf4j
@Component
public class MotionSensorEventHandler extends BaseSensorEventHandler<MotionSensorAvro> {

    public MotionSensorEventHandler(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    protected MotionSensorAvro protoToAvro(SensorEventProto eventProto) {
        log.info("Преобразование SensorEventProto в MotionSensorAvro");

        MotionSensorProto proto = eventProto.getMotionSensorEvent();
        MotionSensorAvro avro = MotionSensorAvro.newBuilder()
                .setLinkQuality(proto.getLinkQuality())
                .setMotion(proto.getMotion())
                .setVoltage(proto.getVoltage())
                .build();

        log.info("Преобразование SensorEventProto в MotionSensorAvro завершено");
        return avro;
    }

    @Override
    public SensorEventProto.PayloadCase getMessageSensorType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }
}
