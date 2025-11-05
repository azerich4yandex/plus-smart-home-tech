package ru.yandex.practicum.service.handler.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.service.producer.CollectorKafkaProducer;

@Slf4j
@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {

    public ScenarioAddedEventHandler(CollectorKafkaProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioAddedEventAvro protoToAvro(HubEventProto eventProto) {
        log.info("Преобразование HubEventProto в ScenarioAddedEventAvro");

        ScenarioAddedEventProto proto = eventProto.getScenarioAdded();
        ScenarioAddedEventAvro avro = ScenarioAddedEventAvro.newBuilder()
                .setName(proto.getName())
                .setActions(proto.getActionList().stream()
                        .map(this::toDeviceActionAvro)
                        .toList())
                .setConditions(proto.getConditionList().stream()
                        .map(this::toConditionAvro)
                        .toList())
                .build();

        log.info("Преобразование HubEventProto в ScenarioAddedEventAvro завершено");
        return avro;
    }

    @Override
    public HubEventProto.PayloadCase getMessageHubType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    private DeviceActionAvro toDeviceActionAvro(DeviceActionProto deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setValue(deviceAction.getValue())
                .setType(ActionTypeAvro.valueOf(deviceAction.getType().name()))
                .build();
    }

    private ScenarioConditionAvro toConditionAvro(ScenarioConditionProto scenarioCondition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setValue(
                        switch (scenarioCondition.getValueCase()) {
                            case INT_VALUE -> scenarioCondition.getIntValue();
                            case BOOL_VALUE -> scenarioCondition.getBoolValue();
                            case VALUE_NOT_SET -> null;
                        }
                )
                .setType(ConditionTypeAvro.valueOf(scenarioCondition.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(scenarioCondition.getOperation().name()))
                .build();
    }
}
