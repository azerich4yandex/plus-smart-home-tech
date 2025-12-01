package ru.yandex.practicum.service.handler.hub;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.repository.ActionRepository;
import ru.yandex.practicum.repository.ConditionRepository;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.repository.SensorRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioAddedHandler implements HubEventHandler {

    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;

    @Override
    public void handleEvent(HubEventAvro event) {
        log.info("Вызвано событие добавления сценария: {}", event);
        ScenarioAddedEventAvro scenarioAddedEventAvro = (ScenarioAddedEventAvro) event.getPayload();
        List<DeviceActionAvro> actionsAvro = scenarioAddedEventAvro.getActions();
        List<ScenarioConditionAvro> conditionsAvro = scenarioAddedEventAvro.getConditions();

        log.info("действия: {}", actionsAvro.toString());
        log.info("условия: {}", conditionsAvro.toString());

        if (scenarioRepository.findByHubIdAndName(event.getHubId(), scenarioAddedEventAvro.getName()).isEmpty()) {
            Scenario scenario = Scenario.builder()
                    .name(scenarioAddedEventAvro.getName())
                    .hubId(event.getHubId())
                    .build();
            scenario = scenarioRepository.save(scenario);
            if (sensorRepository.existsByIdInAndHubId(scenarioAddedEventAvro.getActions().stream()
                    .map(DeviceActionAvro::getSensorId)
                    .collect(Collectors.toList()), event.getHubId())) {
                log.info("Подготовлен новый сценарий: {}", scenario);
                actionRepository.saveAll(mapToActions(actionsAvro, scenario));
            }
            if (sensorRepository.existsByIdInAndHubId(scenarioAddedEventAvro.getConditions().stream()
                    .map(ScenarioConditionAvro::getSensorId)
                    .collect(Collectors.toList()), event.getHubId())) {
                conditionRepository.saveAll(mapToConditions(conditionsAvro, scenario));
            }
        } else {
            Scenario scenario = scenarioRepository
                    .findByHubIdAndName(event.getHubId(), scenarioAddedEventAvro.getName()).get();
            log.info("Сценарий уже существует: {}", scenario);
            if (sensorRepository.existsByIdInAndHubId(scenarioAddedEventAvro.getActions().stream()
                    .map(DeviceActionAvro::getSensorId)
                    .toList(), event.getHubId())) {
                actionRepository.saveAll(mapToActions(actionsAvro, scenario));
            }
            if (sensorRepository.existsByIdInAndHubId(scenarioAddedEventAvro.getConditions().stream()
                    .map(ScenarioConditionAvro::getSensorId)
                    .toList(), event.getHubId())) {
                conditionRepository.saveAll(mapToConditions(conditionsAvro, scenario));
            }
        }
    }

    @Override
    public String getEventType() {
        return ScenarioAddedEventAvro.class.getSimpleName();
    }

    private List<Action> mapToActions(List<DeviceActionAvro> actionsAvro, Scenario scenario) {
        List<Action> actions = actionsAvro.stream()
                .map(actionAvro -> Action.builder()
                        .sensor(sensorRepository.findById(actionAvro.getSensorId()).orElseThrow())
                        .scenario(scenario)
                        .type(actionAvro.getType())
                        .value(actionAvro.getValue() == null ? 0 : actionAvro.getValue())
                        .build())
                .collect(Collectors.toList());
        for (Action action : actions) {
            log.info("Преобразование действий сценария: {}", action.getScenario().getName());
        }
        return actions;
    }

    private List<Condition> mapToConditions(List<ScenarioConditionAvro> conditionsAvro, Scenario scenario) {
        return conditionsAvro.stream()
                .map(conditionAvro -> Condition.builder()
                        .sensor(sensorRepository.findById(conditionAvro.getSensorId()).orElseThrow())
                        .operation(conditionAvro.getOperation())
                        .scenario(scenario)
                        .type(conditionAvro.getType())
                        .value(mapConditionValue(conditionAvro.getValue()))
                        .build())
                .collect(Collectors.toList());
    }

    private int mapConditionValue(Object value) {
        if (value instanceof Integer) {
            return (int) value;
        } else {
            return (Boolean) value ? 1 : 0;
        }
    }
}
