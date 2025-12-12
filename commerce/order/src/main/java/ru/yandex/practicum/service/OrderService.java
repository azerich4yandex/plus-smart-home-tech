package ru.yandex.practicum.service;

import java.util.List;
import java.util.UUID;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.ProductReturnRequest;

public interface OrderService {

    List<OrderDto> getClientOrders(String usrName);

    OrderDto createNewOrder(CreateNewOrderRequest request, String userName);

    OrderDto productReturn(ProductReturnRequest request);

    OrderDto payment(UUID orderId);

    OrderDto paymentFailed(UUID orderId);

    OrderDto delivery(UUID orderId);

    OrderDto deliveryFailed(UUID orderId);

    OrderDto complete(UUID orderId);

    OrderDto calculateTotalCost(UUID orderId);

    OrderDto calculateDeliveryCost(UUID orderId);

    OrderDto assembly(UUID orderId);

    OrderDto assemblyFailed(UUID orderId);
}