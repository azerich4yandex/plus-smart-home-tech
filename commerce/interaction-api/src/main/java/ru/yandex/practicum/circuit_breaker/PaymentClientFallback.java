package ru.yandex.practicum.circuit_breaker;

import java.util.UUID;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.client.PaymentClient;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.exceptions.ServiceFallBackException;

import static ru.yandex.practicum.enums.FallBackUtil.FALLBACK_MESSAGE;

@Component
public class PaymentClientFallback implements PaymentClient {

    @Override
    public PaymentDto processPayment(OrderDto orderDto) {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }

    @Override
    public Double getTotalCost(OrderDto orderDto) {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }

    @Override
    public void emulatePaymentSuccess(UUID paymentId) {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }

    @Override
    public Double getProductsCost(OrderDto orderDto) {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }

    @Override
    public void emulatePaymentFailed(UUID paymentId) {
        throw new ServiceFallBackException(FALLBACK_MESSAGE.toString());
    }
}