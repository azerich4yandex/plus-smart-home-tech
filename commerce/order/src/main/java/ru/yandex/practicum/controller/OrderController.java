package ru.yandex.practicum.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getClientOrders(@RequestParam String userName) {
        log.info("Get orders for user {}", userName);
        return orderService.getClientOrders(userName);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createNewOrder(@RequestBody CreateNewOrderRequest request, @RequestParam String userName) {
        log.info("Create new order for user {}", userName);
        return orderService.createNewOrder(request, userName);
    }

    @PostMapping("/return")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto productReturn(@RequestBody ProductReturnRequest request) {
        log.info("Product return request {}", request.toString());
        return orderService.productReturn(request);
    }

    @PostMapping("/payment")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto payment(@RequestBody UUID orderId) {
        log.info("Payment, orderId: {}", orderId);
        return orderService.payment(orderId);
    }

    @PostMapping("/payment/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto paymentFailed(@RequestBody UUID orderId) {
        log.info("Payment failed, orderId {}", orderId);
        return orderService.paymentFailed(orderId);
    }

    @PostMapping("/delivery")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto delivery(@RequestBody UUID orderId) {
        log.info("Delivery, orderId {}", orderId);
        return orderService.delivery(orderId);
    }

    @PostMapping("/delivery/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto deliveryFailed(@RequestBody UUID orderId) {
        log.info("Delivery failed, orderId: {}", orderId);
        return orderService.deliveryFailed(orderId);
    }

    @PostMapping("/complete")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto complete(@RequestBody UUID orderId) {
        log.info("Complete order, orderId: {}", orderId);
        return orderService.complete(orderId);
    }

    @PostMapping("/calculate/total")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto calculateTotalCost(@RequestBody UUID orderId) {
        log.info("Calculate total cost, orderId: {}", orderId);
        return orderService.calculateTotalCost(orderId);
    }

    @PostMapping("/calculate/delivery")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto calculateDeliveryCost(@RequestBody UUID orderId) {
        log.info("Calculate delivery cost, orderId: {}", orderId);
        return orderService.calculateDeliveryCost(orderId);
    }

    @PostMapping("/assembly")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto assembly(@RequestBody UUID orderId) {
        log.info("Assembly, orderId: {}", orderId);
        return orderService.assembly(orderId);
    }

    @PostMapping("/assembly/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto assemblyFailed(@RequestBody UUID orderId) {
        log.info("Assembly failed, orderId: {}", orderId);
        return orderService.assemblyFailed(orderId);
    }


}