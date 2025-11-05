package ru.yandex.practicum.service.handler.sensor;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.service.producer.CollectorKafkaProducer;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {

    private final CollectorKafkaProducer producer;

    @Value("${collector.topics.sensors}")
    protected String kafkaTopic;

    protected abstract T protoToAvro(SensorEventProto eventProto);

    /**
     * Базовая обработка событий {@link SensorEventProto}
     */
    @Override
    public void handleEvent(SensorEventProto eventProto) {
        log.info("Создание события SensorEvent на уровне сервиса");

        if (!eventProto.getPayloadCase().equals(getMessageSensorType())) {
            throw new IllegalArgumentException("Неизвестный тип события " + eventProto.getPayloadCase());
        }
        log.info("Передано событие SensorEventProto: {}", eventProto.getPayloadCase());

        T payload = protoToAvro(eventProto);

        Instant timestamp = Instant.ofEpochSecond(
                eventProto.getTimestamp().getSeconds(),
                eventProto.getTimestamp().getNanos());

        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(eventProto.getId())
                .setHubId(eventProto.getHubId())
                .setTimestamp(timestamp)
                .setPayload(payload)
                .build();

        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(kafkaTopic, eventAvro);
        producer.send(record);

        log.info("Возврат результатов создания события SensorEvent на уровень контроллера");
    }
}