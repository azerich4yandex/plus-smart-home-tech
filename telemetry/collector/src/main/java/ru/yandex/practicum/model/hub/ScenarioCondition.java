package ru.yandex.practicum.model.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.model.hub.enums.ConditionOperation;
import ru.yandex.practicum.model.hub.enums.ConditionType;

/**
 * Условие сценария, которое содержит информацию о датчике, типе условия, операции и значении.
 */
@Getter
@Setter
public class ScenarioCondition {

    /**
     * Идентификатор датчика, связанного с условием.
     */
    @NotBlank
    private String sensorId;

    /**
     * Тип условия
     */
    @NotNull
    private ConditionType type;

    /**
     * Операция
     */
    @NotNull
    private ConditionOperation operation;

    /**
     * Значение, используемое в условии.
     */
    private Integer value;
}
