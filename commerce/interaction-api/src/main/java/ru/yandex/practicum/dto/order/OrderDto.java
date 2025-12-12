package ru.yandex.practicum.dto.order;

import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.enums.OrderState;

@Getter
@Setter
@ToString
public class OrderDto {

    @NotNull
    private UUID orderId;

    private UUID shoppingCartId;

    @NotNull
    private Map<UUID, Long> products;

    private UUID paymentId;

    private UUID deliveryId;

    private OrderState state;

    private Double deliveryWeight;

    private Double deliveryVolume;

    private Boolean fragile;

    private Double totalPrice;

    private Double deliveryPrice;

    private Double productPrice;
}