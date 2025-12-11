package ru.yandex.practicum.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.circuit_breaker.DeliveryClientFallback;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;

@FeignClient(name = "delivery", path = "/api/v1/delivery", fallback = DeliveryClientFallback.class)
public interface DeliveryClient {

    @PutMapping
    DeliveryDto planDelivery(@RequestBody DeliveryDto deliveryDto);

    @PostMapping("/successful")
    void deliverySuccessful(@RequestBody UUID deliveryId);

    @PostMapping("/picked")
    void deliveryPicked(UUID deliveryId);

    @PostMapping("/failed")
    void deliveryFailed(UUID deliveryId);

    @PostMapping("/cost")
    Double deliveryCost(OrderDto orderDto);
}