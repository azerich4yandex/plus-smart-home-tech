package ru.yandex.practicum.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import ru.yandex.practicum.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;

@Getter
@ToString
public class CreateNewOrderRequest {

    @NotNull
    private ShoppingCartDto shoppingCart;

    @NotNull
    private AddressDto deliveryAddress;
}