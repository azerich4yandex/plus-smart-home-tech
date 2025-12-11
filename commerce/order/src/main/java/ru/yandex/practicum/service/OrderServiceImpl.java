package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.client.DeliveryClient;
import ru.yandex.practicum.client.PaymentClient;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.ProductReturnRequest;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.enums.DeliveryState;
import ru.yandex.practicum.enums.OrderState;
import ru.yandex.practicum.exceptions.NoOrderFoundException;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.repository.OrderRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;
    private final WarehouseClient warehouseClient;

    @Override
    @Cacheable(cacheNames = "orders")
    public List<OrderDto> getClientOrders(String userName) {
        checkUser(userName);
        log.info("Get client orders {}", userName);
        return orderRepository.findAllByUserName(userName).stream()
                .map(orderMapper::toDto).toList();
    }

    @Override
    public OrderDto createNewOrder(CreateNewOrderRequest request, String userName) {
        checkUser(userName);
        BookedProductsDto bookedProductsDto = warehouseClient.checkQuantity(request.getShoppingCart());
        log.info("Booked products for order: {}", bookedProductsDto);
        Order order = orderMapper.toEntity(request, userName, bookedProductsDto);
        orderRepository.save(order);
        log.info("Order created: {}", order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto productReturn(ProductReturnRequest request) {
        Order order = checkAndReturnOrder(request.getOrderId());
        warehouseClient.acceptReturn(order.getProducts());
        log.info("Order {} is returned", order);
        order.setState(OrderState.PRODUCT_RETURNED);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto payment(UUID orderId) {
        Order order = checkAndReturnOrder(orderId);
        PaymentDto paymentDto = paymentClient.processPayment(orderMapper.toDto(order));
        order.setPaymentId(paymentDto.getPaymentId());
        order.setState(OrderState.ON_PAYMENT);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto paymentFailed(UUID orderId) {
        Order order = checkAndReturnOrder(orderId);
        if (order.getState() == OrderState.ON_PAYMENT) {
            order.setState(OrderState.PAYMENT_FAILED);
        } else {
            log.warn("Order {} is not ON PAYMENT state", orderId);
        }
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto delivery(UUID orderId) {
        Order order = checkAndReturnOrder(orderId);
        AddressDto addressFrom = warehouseClient.getWarehouseAddress();
        DeliveryDto deliveryDto = DeliveryDto.builder()
                .orderId(orderId)
                .deliveryState(DeliveryState.CREATED)
                .toAddress(orderMapper.toAddressDto(order.getDeliveryAddress()))
                .fromAddress(addressFrom)
                .build();
        deliveryDto = deliveryClient.planDelivery(deliveryDto);
        log.info("Delivery plan: {}", deliveryDto);
        order.setDeliveryId(deliveryDto.getDeliveryId());
        order.setState(OrderState.ON_DELIVERY);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto deliveryFailed(UUID orderId) {
        Order order = checkAndReturnOrder(orderId);
        order.setState(OrderState.DELIVERY_FAILED);
        log.info("Order is not delivered: {}", order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto complete(UUID orderId) {
        Order order = checkAndReturnOrder(orderId);
        order.setState(OrderState.COMPLETED);
        log.info("Order is completed: {}", order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto calculateTotalCost(UUID orderId) {
        Order order = checkAndReturnOrder(orderId);
        Double totalCost = paymentClient.getTotalCost(orderMapper.toDto(order));
        order.setTotalPrice(totalCost);
        log.info("Total cost for order {} is {}", orderId, totalCost);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) {
        Order order = checkAndReturnOrder(orderId);
        Double deliveryCost = deliveryClient.deliveryCost(orderMapper.toDto(order));
        order.setDeliveryPrice(deliveryCost);
        log.info("Delivery cost for order {} is {}", orderId, deliveryCost);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        Order order = checkAndReturnOrder(orderId);
        AssemblyProductsForOrderRequest assemblyRequest = orderMapper.toAssemblyRequest(order);
        BookedProductsDto bookedProductsDto = warehouseClient.assemblyProductsForOrder(assemblyRequest);
        order.setDeliveryVolume(bookedProductsDto.getDeliveryVolume());
        order.setDeliveryWeight(bookedProductsDto.getDeliveryWeight());
        order.setIsFragile(bookedProductsDto.isFragile());
        order.setState(OrderState.ASSEMBLED);
        log.info("Order is assembled: {}", order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto assemblyFailed(UUID orderId) {
        Order order = checkAndReturnOrder(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        log.warn("Order is not assembled; {} ", order);
        return orderMapper.toDto(order);
    }

    private void checkUser(String userName) {
        if (userName.isBlank()) {
            throw new IllegalArgumentException("User name is null");
        }
    }

    private Order checkAndReturnOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Order with id %s not found".formatted(orderId)));
    }
}