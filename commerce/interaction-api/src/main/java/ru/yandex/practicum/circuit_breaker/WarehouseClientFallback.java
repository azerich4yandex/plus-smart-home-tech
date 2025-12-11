package ru.yandex.practicum.circuit_breaker;

import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.exceptions.ServiceFallBackException;

import static ru.yandex.practicum.enums.FallBackUtil.FALLBACK_MESSAGE;

@Component
public class WarehouseClientFallback implements WarehouseClient {

    @Override
    public void addNewProduct(NewProductInWarehouseRequest request) {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }

    @Override
    public void addProductQuantity(AddProductToWarehouseRequest request) {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }

    @Override
    public BookedProductsDto checkQuantity(ShoppingCartDto shoppingCartDto) {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }

    @Override
    public AddressDto getWarehouseAddress() {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }

    @Override
    public void acceptReturn(Map<UUID, Long> products) {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest request) {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }

    @Override
    public BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest request) {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }
}