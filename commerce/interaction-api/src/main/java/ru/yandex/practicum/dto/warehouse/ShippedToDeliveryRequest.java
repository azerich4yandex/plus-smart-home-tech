package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ShippedToDeliveryRequest {

    @NotNull
    private UUID orderId;

    @NotNull
    private UUID deliveryId;

}