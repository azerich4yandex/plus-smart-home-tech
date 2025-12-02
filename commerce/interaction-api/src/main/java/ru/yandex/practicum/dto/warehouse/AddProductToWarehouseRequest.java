package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AddProductToWarehouseRequest {

    @NotNull
    private UUID productId;

    @Positive
    private Long quantity;
}