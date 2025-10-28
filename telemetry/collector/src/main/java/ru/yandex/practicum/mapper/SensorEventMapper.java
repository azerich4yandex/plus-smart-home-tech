package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.model.sensor.ClimateSensorEvent;
import ru.yandex.practicum.model.sensor.LightSensorEvent;
import ru.yandex.practicum.model.sensor.MotionSensorEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.model.sensor.SwitchSensorEvent;
import ru.yandex.practicum.model.sensor.TemperatureSensorEvent;

@UtilityClass
@Slf4j
public class SensorEventMapper {

    public SensorEventAvro toAvro(SensorEvent event) {
        log.info("Преобразование SensorEvent типа {}", event.getType());

        SensorEventAvro.Builder builder = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp());

        switch (event.getType()) {
            case LIGHT_SENSOR_EVENT -> {
                log.debug("Преобразуем LIGHT_SENSOR_EVENT с hubId={}, type={}", event.getHubId(), event.getType());

                LightSensorEvent lightSensor = (LightSensorEvent) event;

                builder.setPayload(
                        LightSensorAvro.newBuilder()
                                .setLinkQuality(lightSensor.getLinkQuality())
                                .setLuminosity(lightSensor.getLuminosity())
                                .build());
            }

            case MOTION_SENSOR_EVENT -> {
                log.debug("Преобразуем MOTION_SENSOR_EVENT с hubId={}, type={}", event.getHubId(), event.getType());

                MotionSensorEvent motionSensor = (MotionSensorEvent) event;

                builder.setPayload(
                        MotionSensorAvro.newBuilder()
                                .setLinkQuality(motionSensor.getLinkQuality())
                                .setMotion(motionSensor.isMotion())
                                .setVoltage(motionSensor.getVoltage())
                                .build());
            }

            case CLIMATE_SENSOR_EVENT -> {
                log.debug("Преобразуем CLIMATE_SENSOR_EVENT с hubId={}, type={}", event.getHubId(), event.getType());

                ClimateSensorEvent climateSensor = (ClimateSensorEvent) event;

                builder.setPayload(
                        ClimateSensorAvro.newBuilder()
                                .setTemperatureC(climateSensor.getTemperatureC())
                                .setHumidity(climateSensor.getHumidity())
                                .setCo2Level(climateSensor.getCo2Level())
                                .build());
            }

            case SWITCH_SENSOR_EVENT -> {
                log.debug("Преобразуем SWITCH_SENSOR_EVENT с hubId={}, type={}", event.getHubId(), event.getType());

                SwitchSensorEvent switchSensor = (SwitchSensorEvent) event;

                builder.setPayload(
                        SwitchSensorAvro.newBuilder()
                                .setState(switchSensor.isState())
                                .build());
            }

            case TEMPERATURE_SENSOR_EVENT -> {
                log.debug("Преобразуем TEMPERATURE_SENSOR_EVENT с hubId={}, type={}", event.getHubId(),
                        event.getType());

                TemperatureSensorEvent temperatureSensor = (TemperatureSensorEvent) event;

                builder.setPayload(
                        TemperatureSensorAvro.newBuilder()
                                .setTemperatureC(temperatureSensor.getTemperatureC())
                                .setTemperatureF(temperatureSensor.getTemperatureF())
                                .build());
            }
        }

        log.info("Преобразование SensorEvent типа {} завершено", event.getType());
        return builder.build();
    }
}
