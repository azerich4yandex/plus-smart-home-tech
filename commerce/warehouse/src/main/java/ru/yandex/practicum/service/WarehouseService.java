package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {

    void addNewProduct(NewProductInWarehouseRequest request);

    BookedProductsDto checkQuantity(ShoppingCartDto shoppingCartDto);

    void addProductQuantity(AddProductToWarehouseRequest request);

    AddressDto getWarehouseAddress();
}