package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.enums.DeliveryState;
import ru.yandex.practicum.exceptions.NoDeliveryFoundException;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.repository.DeliveryRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderClient orderClient;

    @Override
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.toEntity(deliveryDto);
        delivery.setDeliveryState(DeliveryState.CREATED);
        log.info("Delivery planned {} ", delivery);
        return deliveryMapper.toDto(deliveryRepository.save(delivery));
    }

    @Override
    public void deliverySuccessful(UUID deliveryId) {
        Delivery delivery = checkAndGetDelivery(deliveryId);
        log.info("Delivery with id {} successful", deliveryId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        orderClient.delivery(delivery.getOrderId());
    }

    @Override
    public void deliveryPicked(UUID deliveryId) {
        Delivery delivery = checkAndGetDelivery(deliveryId);
        log.info("Delivery with id {} picked", deliveryId);
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        orderClient.assembly(delivery.getOrderId());
    }

    @Override
    public void deliveryFailed(UUID deliveryId) {
        Delivery delivery = checkAndGetDelivery(deliveryId);
        log.info("Delivery with id {} failed", deliveryId);
        delivery.setDeliveryState(DeliveryState.FAILED);
        orderClient.deliveryFailed(delivery.getOrderId());
    }

    @Override
    public Double deliveryCost(OrderDto orderDto) {
        double deliveryCost = 5.0;
        Delivery delivery = deliveryRepository.findByOrderId(orderDto.getDeliveryId())
                .orElseThrow(() -> new NoDeliveryFoundException("Delivery with id %s for order with id %s not found"
                        .formatted(orderDto.getDeliveryId(), orderDto.getOrderId())));

        delivery.setFragile(orderDto.getFragile());
        delivery.setTotalWeight(orderDto.getDeliveryWeight());
        delivery.setTotalVolume(orderDto.getDeliveryVolume());

        Address warehouseAddress = delivery.getFromAddress();
        if (warehouseAddress.toString().contains("ADDRESS_2")) {
            deliveryCost *= 2;
        }
        if (delivery.isFragile()) {
            deliveryCost += deliveryCost * 0.2;
        }

        deliveryCost += delivery.getTotalWeight() * 0.3;
        deliveryCost += delivery.getTotalVolume() * 0.2;

        if (!delivery.getToAddress().getStreet().equals(warehouseAddress.getStreet())) {
            deliveryCost += deliveryCost * 0.2;
        }
        log.info("Delivery cost calculated: {}", deliveryCost);
        return deliveryCost;
    }

    private Delivery checkAndGetDelivery(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException("Delivery with id %s not found".formatted(deliveryId)));
    }
}