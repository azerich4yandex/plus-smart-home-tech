package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.client.ShoppingStoreClient;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.dto.payment.PaymentState;
import ru.yandex.practicum.exceptions.NoOrderFoundException;
import ru.yandex.practicum.exceptions.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.repository.PaymentRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Value("${payment.vatRate}")
    private double vatRate;

    @Override
    @Transactional
    public PaymentDto processPayment(OrderDto order) {
        checkOrder(order);
        Payment payment = paymentRepository
                .save(paymentMapper
                        .toEntity(order.getOrderId(),
                                order.getTotalPrice(),
                                order.getDeliveryPrice(),
                                order.getTotalPrice(),
                                order.getTotalPrice() * vatRate));
        log.info("Payment processed successfully, payment id: {}", payment.getId());
        return paymentMapper.toDto(payment);
    }

    @Override
    public Double getTotalCost(OrderDto order) {
        checkOrder(order);
        log.info("Total cost calculated, order id: {}", order.getOrderId());
        return order.getDeliveryPrice() + order.getProductPrice() * vatRate + order.getProductPrice();
    }

    @Override
    @Transactional
    public void emulatePaymentSuccess(UUID paymentId) {
        Payment payment = checkPayment(paymentId);
        payment.setPaymentState(PaymentState.SUCCESS);
        log.info("Change payment state to success, payment id: {}", paymentId);
        orderClient.payment(payment.getOrderId());
    }

    @Override
    public Double getProductsCost(OrderDto order) {
        checkOrder(order);
        return order.getProducts().entrySet().stream()
                .mapToDouble(entry -> shoppingStoreClient
                        .getProductById(entry.getKey()).getPrice() * entry.getValue()).sum();
    }

    @Override
    public void emulatePaymentFailed(UUID paymentId) {
        Payment payment = checkPayment(paymentId);
        payment.setPaymentState(PaymentState.FAILED);
        log.info("Change payment state to failed, payment id: {}", paymentId);
        orderClient.paymentFailed(payment.getOrderId());
    }

    private void checkOrder(OrderDto order) {
        if (order.getTotalPrice() == null || order.getDeliveryPrice() == null || order.getOrderId() == null
                || order.getProductPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Not enough info for processing payment, order id: %s"
                    .formatted(order.getOrderId()));
        }
    }

    private Payment checkPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoOrderFoundException("Payment with id: %s not found".formatted(paymentId)));
    }
}