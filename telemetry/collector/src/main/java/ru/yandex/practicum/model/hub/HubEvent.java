package ru.yandex.practicum.model.hub;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.model.hub.enums.HubEventType;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = HubEvent.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAddedEvent.class, name = "DEVICE_ADDED"),
        @JsonSubTypes.Type(value = DeviceRemovedEvent.class, name = "DEVICE_REMOVED"),
        @JsonSubTypes.Type(value = ScenarioAddedEvent.class, name = "SCENARIO_ADDED"),
        @JsonSubTypes.Type(value = ScenarioRemovedEvent.class, name = "SCENARIO_REMOVED")
})
@Getter
@Setter
public abstract class HubEvent {

    /**
     * Идентификатор хаба, связанный с событием.
     */
    @NotBlank
    private String hubId;

    /**
     * Временная метка события. По умолчанию устанавливается текущее время.
     */
    @NotNull
    private Instant timestamp = Instant.now();

    /**
     * Тип события хаба
     */
    @NotNull
    public abstract HubEventType getType();
}
