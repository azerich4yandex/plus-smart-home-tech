package ru.yandex.practicum.dto.shoppingcart;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ChangeProductQuantityRequest {

    @NotNull
    private UUID productId;

    @NotNull
    private Long newQuantity;
}