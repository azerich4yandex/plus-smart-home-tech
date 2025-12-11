package ru.yandex.practicum.service;

import java.util.UUID;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;

public interface PaymentService {

    PaymentDto processPayment(OrderDto order);

    Double getTotalCost(OrderDto order);

    void emulatePaymentSuccess(UUID paymentId);

    Double getProductsCost(OrderDto order);

    void emulatePaymentFailed(UUID paymentId);
}