package ru.yandex.practicum.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.circuit_breaker.PaymentClientFallback;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;

@FeignClient(name = "payment", path = "/api/v1/payment", fallback = PaymentClientFallback.class)
public interface PaymentClient {

    @PostMapping
    PaymentDto processPayment(@RequestBody OrderDto orderDto);

    @PostMapping("/totalCost")
    Double getTotalCost(@RequestBody OrderDto orderDto);

    @PostMapping("/refund")
    void emulatePaymentSuccess(@RequestBody UUID paymentId);

    @PostMapping("/productCost")
    Double getProductsCost(@RequestBody OrderDto orderDto);

    @PostMapping("/failed")
    void emulatePaymentFailed(@RequestBody UUID paymentId);
}