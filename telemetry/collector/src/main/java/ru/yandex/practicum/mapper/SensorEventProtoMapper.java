package ru.yandex.practicum.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.model.sensor.ClimateSensorEvent;
import ru.yandex.practicum.model.sensor.LightSensorEvent;
import ru.yandex.practicum.model.sensor.MotionSensorEvent;
import ru.yandex.practicum.model.sensor.SwitchSensorEvent;
import ru.yandex.practicum.model.sensor.TemperatureSensorEvent;

@UtilityClass
@Slf4j
public class SensorEventProtoMapper {

    public ClimateSensorEvent toClimateSensorEvent(SensorEventProto eventProto) {
        log.info("Преобразование SensorEventProto в ClimateSensorEvent");

        ClimateSensorEvent event = new ClimateSensorEvent();
        event.setTemperatureC(eventProto.getClimateSensorEvent().getTemperatureC());
        event.setHumidity(eventProto.getClimateSensorEvent().getHumidity());
        event.setCo2Level(eventProto.getClimateSensorEvent().getCo2Level());

        log.info("Преобразование SensorEventProto в ClimateSensorEvent завершено");
        return event;
    }

    public LightSensorEvent toLightSensorEvent(SensorEventProto eventProto) {
        log.info("Преобразование SensorEventProto в LightSensorEvent");

        LightSensorEvent event = new LightSensorEvent();
        event.setLinkQuality(eventProto.getLightSensorEvent().getLinkQuality());
        event.setLuminosity(eventProto.getLightSensorEvent().getLuminosity());

        log.info("Преобразование SensorEventProto в LightSensorEvent завершено");
        return event;
    }

    public MotionSensorEvent toMotionSensorEvent(SensorEventProto eventProto) {
        log.info("Преобразование SensorEventProto в MotionSensorEvent");

        MotionSensorEvent event = new MotionSensorEvent();
        event.setLinkQuality(eventProto.getMotionSensorEvent().getLinkQuality());
        event.setMotion(eventProto.getMotionSensorEvent().getMotion());
        event.setVoltage(eventProto.getMotionSensorEvent().getVoltage());

        log.info("Преобразование SensorEventProto в MotionSensorEvent завершено");
        return event;
    }

    public SwitchSensorEvent toSwitchSensorEvent(SensorEventProto eventProto) {
        log.info("Преобразование SensorEventProto в SwitchSensorEvent");

        SwitchSensorEvent event = new SwitchSensorEvent();
        event.setState(eventProto.getSwitchSensorEventOrBuilder().getState());

        log.info("Преобразование SensorEventProto в SwitchSensorEvent завершено");
        return event;
    }

    public TemperatureSensorEvent toTemperatureSensorEvent(SensorEventProto eventProto) {
        log.info("Преобразование SensorEventProto в TemperatureSensorEvent");

        TemperatureSensorEvent event = new TemperatureSensorEvent();
        event.setTemperatureC(eventProto.getTemperatureSensorEvent().getTemperatureC());
        event.setTemperatureF(eventProto.getTemperatureSensorEvent().getTemperatureF());

        log.info("Преобразование SensorEventProto в TemperatureSensorEvent завершено");
        return event;
    }
}