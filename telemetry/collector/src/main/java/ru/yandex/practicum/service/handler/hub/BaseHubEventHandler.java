package ru.yandex.practicum.service.handler.hub;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.service.producer.CollectorKafkaProducer;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {

    private final CollectorKafkaProducer producer;

    @Value("${collector.topics.hubs}")
    protected String kafkaTopic;

    protected abstract T protoToAvro(HubEventProto eventProto);

    /**
     * Базовая обработка событий {@link HubEventProto}
     */
    @Override
    public void handleEvent(HubEventProto eventProto) {
        log.info("Создание события HubEvent на уровне сервиса");

        if (!eventProto.getPayloadCase().equals(getMessageHubType())) {
            throw new IllegalArgumentException("Неизвестный тип события + " + eventProto.getPayloadCase());
        }
        log.info("Передано событие HubEventProto: {}", eventProto.getPayloadCase());

        T payload = protoToAvro(eventProto);

        Instant timestamp = Instant.ofEpochSecond(
                eventProto.getTimestamp().getSeconds(),
                eventProto.getTimestamp().getNanos());

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(eventProto.getHubId())
                .setTimestamp(timestamp)
                .setPayload(payload)
                .build();

        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(kafkaTopic, eventAvro);
        producer.send(record);

        log.info("Возврат результатов создания события HubEvent на уровень контроллера");
    }
}