package ru.yandex.practicum.service.handler.sensor;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.model.sensor.SensorEvent;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseSensorEventHandler implements SensorEventHandler {

    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

    @Value("${collector.topics.sensors}")
    protected String kafkaTopic;

    protected abstract SensorEvent mapSensorEventProtoToModel(SensorEventProto eventProto);

    protected abstract SensorEventAvro mapModelToSensorEventAvro(SensorEvent event);

    /**
     * Базовая обработка событий {@link SensorEventProto}
     */
    @Override
    public void handle(SensorEventProto eventProto) {
        log.info("Создания события SensorEvent на уровне сервиса");

        if (eventProto == null) {
            throw new IllegalArgumentException("SensorEvent не может быть пустым");
        }
        log.info("Передано событие SensorEventProto: {}", eventProto.getPayloadCase());

        SensorEvent event = mapSensorEventProtoToModel(eventProto);

        sendSensorEvent(event);

        log.info("Возврат результатов создания события SensorEvent на уровень контроллера");
    }

    /**
     * Маппинг базовых полей из {@link SensorEventProto} в {@link SensorEvent}
     */
    protected SensorEvent mapBaseProtoFieldsToModel(SensorEvent event, SensorEventProto eventProto) {
        event.setId((eventProto.getId()));
        event.setHubId(eventProto.getHubId());

        Instant timestamp = Instant.ofEpochSecond(
                eventProto.getTimestamp().getSeconds(),
                eventProto.getTimestamp().getNanos());
        event.setTimestamp(timestamp);
        return event;
    }

    /**
     * Отправка обработанного сообщения {@link SensorEvent} в Kafka-топик
     */
    private void sendSensorEvent(SensorEvent event) {
        log.info("Отправка данных SensorEvent с типом {} в Kafka-топик {}", event.getType(), kafkaTopic);

        SensorEventAvro eventAvro = mapModelToSensorEventAvro(event);

        log.info("Передача результатов обработки в SensorEvent-топик {}", kafkaTopic);
        try {
            ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                    kafkaTopic,
                    null,
                    event.getTimestamp().toEpochMilli(),
                    event.getHubId(),
                    eventAvro);

            kafkaTemplate.send(record);
            log.info("Запись о событии SensorEvent успешно отправлена в топик [{}]", kafkaTopic);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при отправке данных в топик [" + kafkaTopic + "]", e);
        }
        log.info("Передача результатов обработки в SensorEvent-топик {} завершена", kafkaTopic);
    }

    /**
     * Маппинг базовых полей из {@link  SensorEvent} в {@link SensorEventAvro}
     */
    protected SensorEventAvro mapBaseModelFieldsToAvro(SensorEvent event, SpecificRecordBase payload) {
        return SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
    }
}