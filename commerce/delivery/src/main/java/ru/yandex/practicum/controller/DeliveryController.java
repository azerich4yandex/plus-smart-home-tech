package ru.yandex.practicum.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

@Slf4j
@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DeliveryDto planDelivery(@RequestBody DeliveryDto deliveryDto) {
        log.info("Plan delivery for {}", deliveryDto);
        return deliveryService.planDelivery(deliveryDto);
    }

    @PostMapping("/successful")
    @ResponseStatus(HttpStatus.OK)
    public void deliverySuccessful(@RequestBody UUID deliveryId) {
        log.info("Emulate: Delivery {} successful", deliveryId);
        deliveryService.deliverySuccessful(deliveryId);
    }

    @PostMapping("/picked")
    @ResponseStatus(HttpStatus.OK)
    public void deliveryPicked(UUID deliveryId) {
        log.info("Emulate: Delivery {} picked", deliveryId);
        deliveryService.deliveryPicked(deliveryId);
    }

    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.OK)
    public void deliveryFailed(UUID deliveryId) {
        log.info("Emulate: Delivery {} failed", deliveryId);
        deliveryService.deliveryFailed(deliveryId);
    }

    @PostMapping("/cost")
    @ResponseStatus(HttpStatus.OK)
    public Double deliveryCost(OrderDto orderDto) {
        log.info("Calculate delivery cost for {}", orderDto);
        return deliveryService.deliveryCost(orderDto);
    }
}