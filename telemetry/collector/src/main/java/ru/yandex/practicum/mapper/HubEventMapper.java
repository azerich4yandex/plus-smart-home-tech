package ru.yandex.practicum.mapper;

import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.model.hub.DeviceAddedEvent;
import ru.yandex.practicum.model.hub.DeviceRemovedEvent;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.hub.ScenarioAddedEvent;
import ru.yandex.practicum.model.hub.ScenarioRemovedEvent;

@UtilityClass
@Slf4j
public class HubEventMapper {

    public HubEventAvro toAvro(HubEvent event) {
        log.info("Преобразование HubEvent типа {}", event.getType());

        HubEventAvro.Builder builder = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp());

        switch (event.getType()) {
            case DEVICE_ADDED -> {
                log.debug("Преобразуем DEVICE_ADDED с hubId={}, type={}", event.getHubId(), event.getType());

                DeviceAddedEvent added = (DeviceAddedEvent) event;

                builder.setPayload(
                        DeviceAddedEventAvro.newBuilder()
                                .setId(added.getId())
                                .setType(DeviceTypeAvro.valueOf(added.getDeviceType().name()))
                                .build());
            }

            case DEVICE_REMOVED -> {
                log.debug("Преобразуем DEVICE_REMOVED с hubId={}", event.getHubId());

                DeviceRemovedEvent removed = (DeviceRemovedEvent) event;
                builder.setPayload(
                        DeviceRemovedEventAvro.newBuilder()
                                .setId(removed.getId())
                                .build()
                );
            }
            case SCENARIO_ADDED -> {
                log.debug("Преобразуем SCENARIO_ADDED с hubId ={}", event.getHubId());

                ScenarioAddedEvent added = (ScenarioAddedEvent) event;

                builder.setPayload(
                        ScenarioAddedEventAvro.newBuilder()
                                .setName(added.getName())
                                .setConditions(added.getConditions().stream()
                                        .map(c -> ScenarioConditionAvro.newBuilder()
                                                .setSensorId(c.getSensorId())
                                                .setType(ConditionTypeAvro.valueOf(c.getType().name()))
                                                .setOperation(ConditionOperationAvro.valueOf(c.getOperation().name()))
                                                .setValue(c.getValue())
                                                .build())
                                        .collect(Collectors.toList()))
                                .setActions(added.getActions().stream()
                                        .map(a -> DeviceActionAvro.newBuilder()
                                                .setSensorId(a.getSensorId())
                                                .setType(ActionTypeAvro.valueOf(a.getType().name()))
                                                .setValue(a.getValue())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build()
                );
            }
            case SCENARIO_REMOVED -> {
                log.debug("Преобразуем SCENARIO_REMOVED с hubId={}", event.getHubId());

                ScenarioRemovedEvent removed = (ScenarioRemovedEvent) event;

                builder.setPayload(
                        ScenarioRemovedEventAvro.newBuilder()
                                .setName(removed.getName())
                                .build()
                );
            }
        }

        log.info("Преобразование HubEvent типа {} завершено", event.getType());
        return builder.build();
    }
}