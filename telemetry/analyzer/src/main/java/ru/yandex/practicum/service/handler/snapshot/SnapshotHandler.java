package ru.yandex.practicum.service.handler.snapshot;

import com.google.protobuf.Timestamp;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.client.HubRouterClient;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.repository.ActionRepository;
import ru.yandex.practicum.repository.ConditionRepository;
import ru.yandex.practicum.repository.ScenarioRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotHandler {

    private final HubRouterClient hubRouterClient;
    private final ScenarioRepository scenarioRepository;
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;

    public void handleSnapshot(SensorsSnapshotAvro snapshot) {
        log.info("Обработка снимка");
        String hubId = snapshot.getHubId();
        log.info("Передан идентификатор хаба: {}", hubId);
        scenarioRepository.findByHubId(hubId).stream()
                .filter(scenario -> getFilter(scenario, snapshot))
                .forEach(scenario -> send(actionRepository.findAllByScenario(scenario)));
    }

    private boolean getFilter(Scenario scenario, SensorsSnapshotAvro snapshot) {
        for (Condition condition : conditionRepository.findAllByScenario(scenario)) {
            if (!checkCondition(condition, snapshot)) {
                log.info("Условие {} для снимка {} не выполнено", condition, snapshot);
                return false;
            }
        }
        log.info("Сценарий {} вызван", scenario.getName());
        return true;
    }

    private void send(List<Action> actions) {
        log.info("Отправка действий {} в хаб {}", actions, actions.getFirst().getScenario().getHubId());
        for (Action action : actions) {
            DeviceActionRequest request = DeviceActionRequest.newBuilder()
                    .setHubId(action.getScenario().getHubId())
                    .setScenarioName(action.getScenario().getName())
                    .setTimestamp(getNow())
                    .setAction(DeviceActionProto.newBuilder()
                            .setSensorId(action.getSensor().getId())
                            .setType(ActionTypeProto.valueOf(action.getType().name()))
                            .setValue(action.getValue() == null ? 0 : action.getValue())
                            .build())
                    .build();
            log.info("Отправка запроса {}", request);
            hubRouterClient.sendRequest(request);
        }
    }

    private Timestamp getNow() {
        Instant now = Instant.now();
        return Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();
    }

    private boolean checkCondition(Condition condition, SensorsSnapshotAvro snapshot) {
        SensorStateAvro sensorState = snapshot.getSensorsState().get(condition.getSensor().getId());
        if (sensorState == null) return false;
        switch (condition.getType()) {
            case MOTION -> {
                MotionSensorAvro motionSensorAvro = (MotionSensorAvro) sensorState.getData();
                return validate(condition.getOperation(), condition.getValue(), motionSensorAvro.getMotion() ? 1 : 0);
            }
            case SWITCH -> {
                SwitchSensorAvro switchSensorAvro = (SwitchSensorAvro) sensorState.getData();
                return validate(condition.getOperation(), condition.getValue(), switchSensorAvro.getState() ? 1 : 0);
            }
            case CO2LEVEL -> {
                ClimateSensorAvro climateSensorAvro = (ClimateSensorAvro) sensorState.getData();
                return validate(condition.getOperation(), condition.getValue(), climateSensorAvro.getCo2Level());
            }
            case HUMIDITY -> {
                ClimateSensorAvro climateSensorAvro = (ClimateSensorAvro) sensorState.getData();
                return validate(condition.getOperation(), condition.getValue(), climateSensorAvro.getHumidity());
            }
            case LUMINOSITY -> {
                LightSensorAvro lightSensorAvro = (LightSensorAvro) sensorState.getData();
                return validate(condition.getOperation(), condition.getValue(), lightSensorAvro.getLuminosity());
            }
            case TEMPERATURE -> {
                ClimateSensorAvro climateSensorAvro = (ClimateSensorAvro) sensorState.getData();
                return validate(condition.getOperation(), condition.getValue(), climateSensorAvro.getTemperatureC());
            }
            case null -> {
                return false;
            }
        }
    }

    private boolean validate(ConditionOperationAvro conditionOperationAvro, int conditionValue, int sensorValue) {
        return switch (conditionOperationAvro) {
            case EQUALS -> sensorValue == conditionValue;
            case LOWER_THAN -> sensorValue < conditionValue;
            case GREATER_THAN -> sensorValue > conditionValue;
            case null -> false;
        };
    }
}
