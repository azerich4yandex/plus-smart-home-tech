package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

@Slf4j
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public PaymentDto processPayment(@RequestBody @Valid @NotNull OrderDto orderDto) {
        log.info("Create payment for order {}", orderDto);
        return paymentService.processPayment(orderDto);
    }

    @PostMapping("/totalCost")
    @ResponseStatus(HttpStatus.OK)
    public Double getTotalCost(@RequestBody @Valid @NotNull OrderDto orderDto) {
        log.info("Get total cost for order {}", orderDto);
        return paymentService.getTotalCost(orderDto);
    }

    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.OK)
    public void emulatePaymentSuccess(@RequestBody @NotNull UUID paymentId) {
        log.info("emulate payment success for payment {}", paymentId);
        paymentService.emulatePaymentSuccess(paymentId);
    }

    @PostMapping("/productCost")
    @ResponseStatus(HttpStatus.OK)
    public Double getProductsCost(@RequestBody @Valid @NotNull OrderDto orderDto) {
        log.info("Get products cost for order {}", orderDto);
        return paymentService.getProductsCost(orderDto);
    }

    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.OK)
    public void emulatePaymentFailed(@RequestBody @NotNull UUID paymentId) {
        log.info("emulate payment failed for payment {}", paymentId);
        paymentService.emulatePaymentFailed(paymentId);
    }
}