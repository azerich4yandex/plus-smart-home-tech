package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.enums.OrderState;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "fragile", source = "order.isFragile")
    OrderDto toDto(Order order);

    @Mapping(target = "id", ignore = true)
    Address toAddress(AddressDto addressDto);

    AddressDto toAddressDto(Address address);

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "products", source = "order.products")
    AssemblyProductsForOrderRequest toAssemblyRequest(Order order);

    default Order toEntity(CreateNewOrderRequest request, String userName, BookedProductsDto bookedProducts) {
        return Order.builder()
                .state(OrderState.NEW)
                .userName(userName)
                .shoppingCartId(request.getShoppingCart().getShoppingCartId())
                .deliveryAddress(toAddress(request.getDeliveryAddress()))
                .products(request.getShoppingCart().getProducts())
                .isFragile(bookedProducts.isFragile())
                .deliveryVolume(bookedProducts.getDeliveryVolume())
                .deliveryWeight(bookedProducts.getDeliveryWeight())
                .build();
    }
}