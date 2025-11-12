package ru.yandex.practicum.service;

import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto.ValueCase;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.mapper.HubEventAvroMapper;
import ru.yandex.practicum.mapper.SensorEventAvroMapper;
import ru.yandex.practicum.model.hub.DeviceAction;
import ru.yandex.practicum.model.hub.DeviceAddedEvent;
import ru.yandex.practicum.model.hub.DeviceRemovedEvent;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.hub.ScenarioAddedEvent;
import ru.yandex.practicum.model.hub.ScenarioCondition;
import ru.yandex.practicum.model.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.model.hub.enums.ActionType;
import ru.yandex.practicum.model.hub.enums.ConditionOperation;
import ru.yandex.practicum.model.hub.enums.ConditionType;
import ru.yandex.practicum.model.hub.enums.DeviceType;
import ru.yandex.practicum.model.sensor.ClimateSensorEvent;
import ru.yandex.practicum.model.sensor.LightSensorEvent;
import ru.yandex.practicum.model.sensor.MotionSensorEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.model.sensor.SwitchSensorEvent;
import ru.yandex.practicum.model.sensor.TemperatureSensorEvent;

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

    public void handleSensorEvent(SensorEventProto eventProto) {
        log.info("Обработка события SensorEventProto на уровне сервиса");
        log.info("Получено событие SensorEventProto: {}", eventProto.getPayloadCase());

        log.info("Преобразование события SensorEventProto начато");
        SensorEvent event;

        Instant timestamp = Instant.ofEpochSecond(
                eventProto.getTimestamp().getSeconds(),
                eventProto.getTimestamp().getNanos());

        switch (eventProto.getPayloadCase()) {
            case CLIMATE_SENSOR_EVENT -> {
                ClimateSensorEvent e = new ClimateSensorEvent();
                e.setTemperatureC(eventProto.getClimateSensorEvent().getTemperatureC());
                e.setHumidity(eventProto.getClimateSensorEvent().getHumidity());
                e.setCo2Level(eventProto.getClimateSensorEvent().getCo2Level());
                event = e;
            }
            case LIGHT_SENSOR_EVENT -> {
                LightSensorEvent e = new LightSensorEvent();
                e.setLinkQuality(eventProto.getLightSensorEvent().getLinkQuality());
                e.setLuminosity(eventProto.getLightSensorEvent().getLuminosity());
                event = e;
            }
            case MOTION_SENSOR_EVENT -> {
                MotionSensorEvent e = new MotionSensorEvent();
                e.setLinkQuality(eventProto.getMotionSensorEvent().getLinkQuality());
                e.setMotion(eventProto.getMotionSensorEvent().getMotion());
                e.setVoltage(eventProto.getMotionSensorEvent().getVoltage());
                event = e;
            }
            case SWITCH_SENSOR_EVENT -> {
                SwitchSensorEvent e = new SwitchSensorEvent();
                e.setState(eventProto.getSwitchSensorEvent().getState());
                event = e;
            }
            case TEMPERATURE_SENSOR_EVENT -> {
                TemperatureSensorEvent e = new TemperatureSensorEvent();
                e.setTemperatureC(eventProto.getTemperatureSensorEvent().getTemperatureC());
                e.setTemperatureF(eventProto.getTemperatureSensorEvent().getTemperatureF());
                event = e;
            }
            default -> {
                log.warn("Неизвестный payload SensorEventProto: {}", eventProto.getPayloadCase());
                return;
            }
        }

        event.setId(eventProto.getId());
        event.setHubId(eventProto.getHubId());
        event.setTimestamp(timestamp);
        log.info("Преобразование события SensorEventProto завершено. Получено событие {}", event.getType());

        sendSensorEvent(event);
        log.info("Обработка события SensorEventProto на уровне сервиса завершена");

        log.info("Возврат результатов обработки события SensorEventProto на уровень контроллера");
    }

    public void handleHubEvent(HubEventProto eventProto) {
        log.info("Обработка события HubEventProto на уровне сервиса");
        log.info("Получено событие HubEventProto: {}", eventProto.getPayloadCase());

        log.info("Преобразование события HubEventProto начато");
        HubEvent event;

        Instant timestamp = Instant.ofEpochSecond(
                eventProto.getTimestamp().getSeconds(),
                eventProto.getTimestamp().getNanos());

        switch (eventProto.getPayloadCase()) {
            case DEVICE_ADDED_EVENT -> {
                DeviceAddedEvent e = new DeviceAddedEvent();

                e.setId(eventProto.getDeviceAddedEvent().getId());
                e.setDeviceType(DeviceType.valueOf(eventProto.getDeviceAddedEvent().getType().name()));
                event = e;
            }
            case DEVICE_REMOVED_EVENT -> {
                DeviceRemovedEvent e = new DeviceRemovedEvent();

                e.setId(eventProto.getDeviceRemovedEvent().getId());
                event = e;
            }
            case SCENARIO_ADDED_EVENT -> {
                ScenarioAddedEvent e = new ScenarioAddedEvent();

                e.setName(eventProto.getScenarioAddedEvent().getName());
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
                e.setConditions(conditions);

                List<DeviceAction> actions = eventProto.getScenarioAddedEvent().getActionList().stream()
                        .map(actionProto -> {
                            DeviceAction action = new DeviceAction();

                            action.setSensorId(actionProto.getSensorId());
                            action.setType(ActionType.valueOf(actionProto.getType().name()));
                            action.setValue(actionProto.hasValue() ? action.getValue() : null);
                            return action;
                        })
                        .toList();
                e.setActions(actions);
                event = e;
            }
            case SCENARIO_REMOVED_EVENT -> {
                ScenarioRemovedEvent e = new ScenarioRemovedEvent();

                e.setName(eventProto.getScenarioRemovedEvent().getName());
                event = e;
            }
            default -> {
                log.warn("Неизвестный payload HubEventProto: {}", eventProto.getPayloadCase());
                return;
            }
        }

        event.setHubId(eventProto.getHubId());
        event.setTimestamp(timestamp);
        log.info("Преобразование события HubEventProto завершено. Получено событие {}", event.getType());

        sendHubEvent(event);
        log.info("Обработка события HubEventProto на уровне сервиса завершена");

        log.info("Возврат результатов обработки события HubEventProto на уровень контроллера");
    }

    private void sendSensorEvent(SensorEvent event) {
        log.info("Создания события SensorEvent на уровне сервиса");

        SensorEventAvro eventAvro = SensorEventAvroMapper.toAvro(event);

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

        log.info("Возврат результатов создания события SensorEvent");
    }

    private void sendHubEvent(HubEvent event) {
        log.info("Создание события HubEvent на уровне сервиса");

        HubEventAvro eventAvro = HubEventAvroMapper.toAvro(event);

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

        log.info("Возврат результатов создания события HubEvent");
    }
}
