package ru.yandex.practicum.service;

import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.service.handler.snapshot.SnapshotHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor {

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);

    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;
    private final SnapshotHandler snapshotHandler;

    @Value("${analyzer.topics.snapshot}")
    private String snapshotTopic;

    public void start() {
        try {
            consumer.subscribe(List.of(snapshotTopic));
            log.info("Подписался на топик {}", snapshotTopic);

            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                if (records.isEmpty()) {
                    continue;
                }
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    log.info("Получено сообщение от консьюмера {}", record.value());
                    snapshotHandler.handleSnapshot(record.value());
                }
                consumer.commitSync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка при обработке события хаба", e);
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
                log.info("Консьюмер закрыт");
            }
        }
    }
}
