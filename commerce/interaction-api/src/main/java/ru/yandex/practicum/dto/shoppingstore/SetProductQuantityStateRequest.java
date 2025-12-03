package ru.yandex.practicum.dto.shoppingstore;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;
import ru.yandex.practicum.enums.QuantityState;

@Getter
@ToString
public class SetProductQuantityStateRequest {

    @NotNull
    private UUID productId;

    @NotNull
    private QuantityState quantityState;
}