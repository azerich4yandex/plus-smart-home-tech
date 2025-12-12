package ru.yandex.practicum.client;

import java.util.Map;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.circuit_breaker.WarehouseClientFallback;
import ru.yandex.practicum.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.ShippedToDeliveryRequest;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse", fallbackFactory = WarehouseClientFallback.class)
public interface WarehouseClient {

    @PutMapping
    void addNewProduct(@RequestBody NewProductInWarehouseRequest request);

    @PostMapping("/add")
    void addProductQuantity(@RequestBody AddProductToWarehouseRequest request);

    @PostMapping("/check")
    BookedProductsDto checkQuantity(@RequestBody ShoppingCartDto shoppingCartDto);

    @GetMapping("/address")
    AddressDto getWarehouseAddress();

    @PostMapping("/return")
    void acceptReturn(@RequestBody Map<UUID, Long> products);

    @PostMapping("/shipped")
    void shippedToDelivery(@RequestBody ShippedToDeliveryRequest request);

    @PostMapping("/assembly")
    BookedProductsDto assemblyProductsForOrder(@RequestBody AssemblyProductsForOrderRequest request);
}