package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.service.WarehouseService;

@Slf4j
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void addNewProduct(@RequestBody @Valid NewProductInWarehouseRequest request) {
        log.info("adding new product {} to warehouse", request);
        warehouseService.addNewProduct(request);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addProductQuantity(@RequestBody @Valid AddProductToWarehouseRequest request) {
        log.info("adding quantity product {} to warehouse", request);
        warehouseService.addProductQuantity(request);
    }

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public BookedProductsDto checkQuantity(@RequestBody @Valid ShoppingCartDto shoppingCartDto) {
        log.info("checking product {} in warehouse", shoppingCartDto);
        return warehouseService.checkQuantity(shoppingCartDto);
    }

    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto getWarehouseAddress() {
        log.info("getting warehouse address");
        return warehouseService.getWarehouseAddress();
    }
}
