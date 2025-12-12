package ru.yandex.practicum.service;

import java.util.UUID;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;

public interface DeliveryService {

    DeliveryDto planDelivery(DeliveryDto deliveryDto);

    void deliverySuccessful(UUID deliveryId);

    void deliveryPicked(UUID deliveryId);

    void deliveryFailed(UUID deliveryId);

    Double deliveryCost(OrderDto orderDto);
}