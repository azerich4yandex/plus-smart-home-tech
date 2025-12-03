package ru.yandex.practicum.mapper;

import java.util.Map;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCart;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {

    @Mapping(target = "shoppingCartId", source = "shoppingCart.id")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userName", source = "userName")
    @Mapping(target = "products", source = "products")
    @Mapping(target = "isActive", source = "isActive")
    ShoppingCart toEntity(String userName, Map<UUID, Long> products, boolean isActive);
}