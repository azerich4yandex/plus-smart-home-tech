package ru.yandex.practicum.service;

import java.util.Map;
import java.util.UUID;
import ru.yandex.practicum.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.ShippedToDeliveryRequest;

public interface WarehouseService {

    void addNewProduct(NewProductInWarehouseRequest request);

    BookedProductsDto checkQuantity(ShoppingCartDto shoppingCartDto);

    void addProductQuantity(AddProductToWarehouseRequest request);

    AddressDto getWarehouseAddress();

    void acceptReturn(Map<UUID, Long> products);

    void shippedToDelivery(ShippedToDeliveryRequest request);

    BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest request);
}