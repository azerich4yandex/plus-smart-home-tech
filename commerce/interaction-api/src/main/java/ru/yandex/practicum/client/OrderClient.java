package ru.yandex.practicum.client;

import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.circuit_breaker.OrderClientFallback;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;

@FeignClient(name = "order-service", path = "/api/v1/order", fallback = OrderClientFallback.class)
public interface OrderClient {

    @GetMapping
    List<OrderDto> getClientOrders(@RequestParam String userName);

    @PutMapping
    OrderDto createNewOrder(@RequestBody CreateNewOrderRequest request, @RequestParam String userName);

    @PostMapping("/payment")
    OrderDto payment(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto paymentFailed(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    OrderDto delivery(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto deliveryFailed(@RequestBody UUID orderId);

    @PostMapping("/assembly")
    OrderDto assembly(@RequestBody UUID orderId);
}