package ru.yandex.practicum.model.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.model.sensor.enums.SensorEventType;

/**
 * Событие датчика движения.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {

    /**
     * Качество связи.
     */
    @NotNull
    private int linkQuality;

    /**
     * Наличие/отсутствие движения.
     */
    @NotNull
    private boolean motion;

    /**
     * Напряжение.
     */
    @NotNull
    private int voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
