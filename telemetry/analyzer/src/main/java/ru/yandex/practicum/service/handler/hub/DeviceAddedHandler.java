package ru.yandex.practicum.service.handler.hub;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.repository.SensorRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceAddedHandler implements HubEventHandler {

    private final SensorRepository sensorRepository;

    @Override
    public void handleEvent(HubEventAvro event) {
        DeviceAddedEventAvro deviceAddedEventAvro = (DeviceAddedEventAvro) event.getPayload();
        log.info("Устройство id: {} в хаб с id: {}  добавлено", deviceAddedEventAvro.getId(), event.getHubId());
        if (!sensorRepository.existsByIdInAndHubId(List.of(deviceAddedEventAvro.getId()), event.getHubId())) {
            Sensor sensor = Sensor.builder()
                    .id(deviceAddedEventAvro.getId())
                    .hubId(event.getHubId())
                    .build();
            sensorRepository.save(sensor);
        }
    }

    @Override
    public String getEventType() {
        return DeviceAddedEventAvro.class.getSimpleName();
    }
}
