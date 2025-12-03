package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DimensionDto {

    @Positive
    private double width;

    @Positive
    private double height;

    @Positive
    private double depth;
}