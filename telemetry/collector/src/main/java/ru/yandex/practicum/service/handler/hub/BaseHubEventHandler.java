package ru.yandex.practicum.service.handler.hub;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.model.hub.HubEvent;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubEventHandler implements HubEventHandler {

    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

    @Value("${collector.topics.hubs}")
    protected String kafkaTopic;

    protected abstract HubEvent mapHubEventProtoToModel(HubEventProto eventProto);

    protected abstract HubEventAvro mapModelToHubEventAvro(HubEvent event);

    /**
     * Базовая обработка событий {@link HubEventProto}
     */
    @Override
    public void handle(HubEventProto eventProto) {
        log.info("Создание события HubEvent на уровне сервиса");

        if (eventProto == null) {
            throw new IllegalArgumentException("HubEvent не может быть пустым");
        }
        log.info("Передано событие HubEventProto: {}", eventProto.getPayloadCase());

        HubEvent event = mapHubEventProtoToModel(eventProto);

        sendHubEvent(event);

        log.info("Возврат результатов создания события HubEvent на уровень контроллера");
    }

    /**
     * Маппинг базовых полей из  {@link HubEventProto} в {@link HubEvent}
     */
    protected HubEvent mapBaseProtoFieldsToModel(HubEvent event, HubEventProto eventProto) {
        event.setHubId(eventProto.getHubId());

        Instant timestamp = Instant.ofEpochSecond(
                eventProto.getTimestamp().getSeconds(),
                eventProto.getTimestamp().getNanos());

        event.setTimestamp(timestamp);
        return event;
    }

    /**
     * Отправка обработанного сообщения {@link HubEvent} в Kafka-топик
     */
    private void sendHubEvent(HubEvent event) {
        log.info("Отправка данных HubEvent с типом {} в Kafka-топик {}", event.getType(), kafkaTopic);

        HubEventAvro eventAvro = mapModelToHubEventAvro(event);

        log.info("Передача результатов обработки в HubEvent-топик {}", kafkaTopic);
        try {
            ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                    kafkaTopic,
                    null,
                    event.getTimestamp().toEpochMilli(),
                    event.getHubId(),
                    eventAvro);
            kafkaTemplate.send(record);
            log.info("Запись о событии HubEvent успешно отправлена в топик [{}]", kafkaTopic);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при отправке результатов в Kafka", e);
        }

        log.info("Возврат результатов создания события HubEvent");
    }

    /**
     * Маппинг базовых полей из {@link HubEvent} в {@link HubEventAvro}
     */
    protected HubEventAvro mapBaseModelFieldsToAvro(HubEvent event, SpecificRecordBase payload) {
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();

    }
}