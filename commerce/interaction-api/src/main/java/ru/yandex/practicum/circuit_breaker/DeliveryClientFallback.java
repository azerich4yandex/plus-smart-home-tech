package ru.yandex.practicum.circuit_breaker;

import java.util.UUID;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.client.DeliveryClient;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.exceptions.ServiceFallBackException;

import static ru.yandex.practicum.enums.FallBackUtil.FALLBACK_MESSAGE;

@Component
public class DeliveryClientFallback implements DeliveryClient {

    @Override
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }

    @Override
    public void deliverySuccessful(UUID deliveryId) {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }

    @Override
    public void deliveryPicked(UUID deliveryId) {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }

    @Override
    public void deliveryFailed(UUID deliveryId) {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }

    @Override
    public Double deliveryCost(OrderDto orderDto) {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }
}