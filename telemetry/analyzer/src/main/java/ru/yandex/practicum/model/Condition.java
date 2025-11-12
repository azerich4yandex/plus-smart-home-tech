package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;

/**
 * Условие
 */
@Entity
@Table(name = "conditions")
@SecondaryTable(name = "scenario_conditions", pkJoinColumns = @PrimaryKeyJoinColumn(name = "condition_id"))
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Condition {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    /**
     * Тип
     */
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    ConditionTypeAvro type;

    /**
     * Операция
     */
    @Column(name = "operation")
    @Enumerated(EnumType.STRING)
    ConditionOperationAvro operation;

    /**
     * Значение
     */
    @Column(name = "value")
    int value;

    /**
     * Сценарий
     */
    @ManyToOne
    @JoinColumn(table = "scenario_conditions", name = "scenario_id")
    Scenario scenario;

    /**
     * Датчик
     */
    @ManyToOne
    @JoinColumn(table = "scenario_conditions", name = "sensor_id")
    Sensor sensor;
}
