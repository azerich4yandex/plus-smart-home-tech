package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "warehouse_items", schema = "warehouse")
@Getter
@Setter
@ToString
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class WarehouseItem {

    @Id
    @Column(name = "id")
    UUID id;

    @Column(name = "quantity")
    @NotNull
    Long quantity;

    @Column(name = "fragile")
    Boolean fragile;

    @Column(name = "weight")
    @NotNull
    Double weight;

    @Column(name = "width")
    @NotNull
    Double width;

    @Column(name = "height")
    @NotNull
    Double height;

    @Column(name = "depth")
    @NotNull
    Double depth;
}
