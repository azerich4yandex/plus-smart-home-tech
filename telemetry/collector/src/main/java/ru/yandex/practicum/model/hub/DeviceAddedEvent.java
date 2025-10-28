package ru.yandex.practicum.model.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.model.hub.enums.DeviceType;
import ru.yandex.practicum.model.hub.enums.HubEventType;

/**
 * Событие, сигнализирующее о добавлении нового устройства в систему.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class DeviceAddedEvent extends HubEvent {


    /**
     * Тип устройства
     */
    @NotNull
    private DeviceType deviceType;

    /**
     * Идентификатор добавленного устройства.
     */
    @NotBlank
    private String id;


    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
