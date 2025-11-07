package ru.yandex.practicum.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

@Component
public class SnapshotProcessor {

    private final Map<String, SensorsSnapshotAvro> snapshotAvroMap = new HashMap<>();

    Optional<SensorsSnapshotAvro> updateState(SensorEventAvro eventAvro) {
        SensorsSnapshotAvro snapshotAvro = snapshotAvroMap.getOrDefault(eventAvro.getHubId(),
                SensorsSnapshotAvro.newBuilder()
                        .setHubId(eventAvro.getHubId())
                        .setTimestamp(eventAvro.getTimestamp())
                        .setSensorsState(new HashMap<>())
                        .build());

        SensorStateAvro oldStateAvro = snapshotAvro.getSensorsState().get(eventAvro.getId());
        if (oldStateAvro != null) {
            if (oldStateAvro.getTimestamp().isAfter(eventAvro.getTimestamp()) || oldStateAvro.getData()
                    .equals(eventAvro.getPayload())) {
                return Optional.empty();
            }
        }

        SensorStateAvro newStateAvro = SensorStateAvro.newBuilder()
                .setTimestamp(eventAvro.getTimestamp())
                .setData(eventAvro.getPayload())
                .build();
        snapshotAvro.getSensorsState().put(eventAvro.getId(), newStateAvro);
        snapshotAvro.setTimestamp(eventAvro.getTimestamp());

        snapshotAvroMap.put(eventAvro.getHubId(), snapshotAvro);

        return Optional.of(snapshotAvro);
    }
}
