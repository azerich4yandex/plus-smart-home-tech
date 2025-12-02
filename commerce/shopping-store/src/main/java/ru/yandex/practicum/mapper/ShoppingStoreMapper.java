package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.shoppingstore.ProductDto;
import ru.yandex.practicum.model.Product;

@Mapper(componentModel = "spring")
public interface ShoppingStoreMapper {

    @Mapping(target = "productId", source = "product.id")
    ProductDto toDto(Product product);

    @Mapping(target = "id", source = "productDto.productId")
    Product toEntity(ProductDto productDto);
}