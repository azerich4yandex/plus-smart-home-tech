package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.model.WarehouseItem;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

    @Mapping(target = "id", source = "request.productId")
    @Mapping(target = "quantity", constant = "0L")
    @Mapping(target = "width", source = "request.dimension.width")
    @Mapping(target = "height", source = "request.dimension.height")
    @Mapping(target = "depth", source = "request.dimension.depth")
    WarehouseItem toEntity(NewProductInWarehouseRequest request);
}
