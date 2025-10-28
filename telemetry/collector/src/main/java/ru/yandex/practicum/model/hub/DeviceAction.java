package ru.yandex.practicum.model.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.model.hub.enums.ActionType;

/**
 * Представляет действие, которое должно быть выполнено устройством.
 */
@Getter
@Setter
public class DeviceAction {

    /**
     * Идентификатор датчика, связанного с действием.
     */
    @NotBlank
    private String sensorId;

    /**
     * Тип события
     */
    @NotNull
    private ActionType type;

    /**
     * Необязательное значение, связанное с действием.
     */
    private Integer value;
}
