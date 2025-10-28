package ru.yandex.practicum.model.hub;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.model.hub.enums.HubEventType;

/**
 * Событие добавления сценария в систему. Содержит информацию о названии сценария, условиях и действиях.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {

    /**
     * Название добавленного сценария. Должно содержать не менее 3 символов.
     */
    @NotBlank
    @Size(min = 3)
    private String name;

    /**
     * Список условий, которые связаны со сценарием. Не может быть пустым.
     */
    @Valid
    @NotEmpty
    private List<ScenarioCondition> conditions;

    /**
     * Список действий, которые должны быть выполнены в рамках сценария. Не может быть пустым.
     */
    @Valid
    @NotEmpty
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
