package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.model.Delivery;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    @Mapping(target = "totalWeight", ignore = true)
    @Mapping(target = "totalVolume", ignore = true)
    @Mapping(target = "fragile", ignore = true)
    @Mapping(target = "id", source = "dto.deliveryId")
    @Mapping(target = "deliveryState", source = "dto.deliveryState")
    Delivery toEntity(DeliveryDto dto);

    @Mapping(target = "deliveryId", source = "delivery.id")
    DeliveryDto toDto(Delivery delivery);
}