package ru.yandex.practicum.mapper;

import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto.ValueCase;
import ru.yandex.practicum.model.hub.DeviceAction;
import ru.yandex.practicum.model.hub.DeviceAddedEvent;
import ru.yandex.practicum.model.hub.DeviceRemovedEvent;
import ru.yandex.practicum.model.hub.ScenarioAddedEvent;
import ru.yandex.practicum.model.hub.ScenarioCondition;
import ru.yandex.practicum.model.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.model.hub.enums.ActionType;
import ru.yandex.practicum.model.hub.enums.ConditionOperation;
import ru.yandex.practicum.model.hub.enums.ConditionType;
import ru.yandex.practicum.model.hub.enums.DeviceType;

@UtilityClass
@Slf4j
public class HubEventProtoMapper {

    public DeviceAddedEvent toDeviceAddedEvent(HubEventProto eventProto) {
        log.info("Преобразование HubEventProto в DeviceAddedEvent");

        DeviceAddedEvent event = new DeviceAddedEvent();
        event.setId(eventProto.getDeviceAddedEvent().getId());
        event.setDeviceType(DeviceType.valueOf(eventProto.getDeviceAddedEvent().getType().name()));

        log.info("Преобразование HubEventProto в DeviceAddedEvent завершено");
        return event;
    }

    public DeviceRemovedEvent toDeviceRemovedEvent(HubEventProto eventProto) {
        log.info("Преобразование HubEventProto в DeviceRemovedEvent");

        DeviceRemovedEvent event = new DeviceRemovedEvent();
        event.setId(eventProto.getDeviceRemovedEvent().getId());

        log.info("Преобразование HubEventProto в DeviceRemovedEvent завершено");
        return event;
    }

    public ScenarioAddedEvent toScenarioAddedEvent(HubEventProto eventProto) {
        log.info("Преобразование HubEventProto в ScenarioAddedEvent");

        ScenarioAddedEvent event = new ScenarioAddedEvent();
        event.setName(eventProto.getScenarioAddedEvent().getName());

        List<ScenarioCondition> conditions = eventProto.getScenarioAddedEvent().getConditionList().stream()
                .map(conditionProto -> {
                    ScenarioCondition condition = new ScenarioCondition();

                    condition.setSensorId(conditionProto.getSensorId());
                    condition.setType(ConditionType.valueOf(conditionProto.getType().name()));
                    condition.setOperation(ConditionOperation.valueOf(conditionProto.getOperation().name()));

                    if (conditionProto.getValueCase() == ValueCase.VALUE_NOT_SET) {
                        condition.setValue(null);
                    } else {
                        switch (conditionProto.getValueCase()) {
                            case BOOL_VALUE -> condition.setValue(conditionProto.getBoolValue() ? 1 : 0);
                            case INT_VALUE -> condition.setValue(conditionProto.getIntValue());
                            default -> throw new IllegalStateException(
                                    "Неизвестное значение value: " + conditionProto.getValueCase());
                        }
                    }
                    return condition;
                })
                .toList();
        event.setConditions(conditions);

        List<DeviceAction> actions = eventProto.getScenarioAddedEvent().getActionList().stream()
                .map(actionProto -> {
                    DeviceAction action = new DeviceAction();

                    action.setSensorId(actionProto.getSensorId());
                    action.setType(ActionType.valueOf(actionProto.getType().name()));
                    action.setValue(actionProto.hasValue() ? action.getValue() : null);
                    return action;
                })
                .toList();
        event.setActions(actions);

        log.info("Преобразование HubEventProto в ScenarioAddedEvent завершено");
        return event;
    }

    public ScenarioRemovedEvent toScenarioRemovedEvent(HubEventProto eventProto) {
        log.info("Преобразование HubEventProto в ScenarioRemovedEvent");

        ScenarioRemovedEvent event = new ScenarioRemovedEvent();
        event.setName(eventProto.getScenarioRemovedEvent().getName());

        log.info("Преобразование HubEventProto в ScenarioRemovedEvent завершено");
        return event;
    }
}
