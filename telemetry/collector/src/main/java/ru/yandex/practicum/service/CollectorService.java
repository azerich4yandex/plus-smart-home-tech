package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.mapper.HubEventMapper;
import ru.yandex.practicum.mapper.SensorEventMapper;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;

/**
 * Сервис обработки событий
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CollectorService {

    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

    @Value("${collector.topics.sensors}")
    private String sensorsTopic;

    @Value("${collector.topics.hubs}")
    private String hubsTopic;

    public void sendSensorEvent(SensorEvent event) {
        log.info("Создания события SensorEvent на уровне сервиса");

        SensorEventAvro eventAvro = SensorEventMapper.toAvro(event);

        log.info("Передача результатов обработки в SensorEvent-топик {}", sensorsTopic);
        try {
            ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                    sensorsTopic,
                    null,
                    event.getTimestamp().toEpochMilli(),
                    event.getHubId(),
                    eventAvro);

            kafkaTemplate.send(record);
            log.info("Запись о событии SensorEvent успешно отправлена в топик [{}]", sensorsTopic);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при отправке данных в топик [" + sensorsTopic + "]", e);
        }

        log.info("Возврат результатов создания события SensorEvent на уровень контроллера");
    }

    public void sendHubEvent(HubEvent event) {
        log.info("Создание события HubEvent на уровне сервиса");

        HubEventAvro eventAvro = HubEventMapper.toAvro(event);

        log.info("Передача результатов обработки в HubEvent-топик {}", hubsTopic);
        try {
            ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                    hubsTopic,
                    null,
                    event.getTimestamp().toEpochMilli(),
                    event.getHubId(),
                    eventAvro);
            kafkaTemplate.send(record);
            log.info("Запись о событии HubEvent успешно отправлена в топик [{}]", hubsTopic);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при отправке результатов в Kafka", e);
        }

        log.info("Возврат результатов создания события HubEvent на уровень контроллера");
    }
}
