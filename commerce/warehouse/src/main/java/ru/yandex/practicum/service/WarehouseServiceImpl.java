package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.exceptions.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exceptions.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.exceptions.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mapper.WarehouseMapper;
import ru.yandex.practicum.model.WarehouseItem;
import ru.yandex.practicum.repository.WarehouseRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    @Transactional
    @Override
    public void addNewProduct(NewProductInWarehouseRequest request) {
        checkProductAlreadyExists(request.getProductId());
        log.info("Product {} added to warehouse", request);
        warehouseRepository.save(warehouseMapper.toEntity(request));
    }

    @Override
    public BookedProductsDto checkQuantity(ShoppingCartDto shoppingCartDto) {
        double weight = 0;
        double volume = 0;
        boolean isFragile = false;
        Map<UUID, Long> products = shoppingCartDto.getProducts();
        Map<UUID, WarehouseItem> warehouseItems = warehouseRepository.findAllById(products.keySet()).stream()
                .collect(Collectors.toMap(WarehouseItem::getId, Function.identity()));
        products.keySet().forEach(productId -> {
            if (!warehouseItems.containsKey(productId)) {
                throw new NoSpecifiedProductInWarehouseException("Product with id: %s not found"
                        .formatted(productId));
            }
        });
        for (Map.Entry<UUID, Long> entry : products.entrySet()) {
            UUID productId = entry.getKey();
            Long quantity = entry.getValue();
            if (warehouseItems.get(productId).getQuantity() < quantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouseException("Product with id: %s quantity: %d is low"
                        .formatted(productId, quantity));
            }
            weight += warehouseItems.get(productId).getWeight() * quantity;
            volume += warehouseItems.get(productId).getWidth() *
                    warehouseItems.get(productId).getWidth() *
                    warehouseItems.get(productId).getDepth() *
                    warehouseItems.get(productId).getHeight() *
                    quantity;
            if (!isFragile && warehouseItems.get(productId).getFragile()) {
                isFragile = true;
            }
        }
        log.info("Booked products by weight: {}, volume: {}, fragile: {}", weight, volume, isFragile);
        return BookedProductsDto.builder()
                .deliveryWeight(weight)
                .deliveryVolume(volume)
                .fragile(isFragile)
                .build();
    }

    @Transactional
    @Override
    public void addProductQuantity(AddProductToWarehouseRequest request) {
        WarehouseItem product = checkProductNotFound(request.getProductId());
        log.info("Product {} quantity increased by {}", product, request.getQuantity());
        product.setQuantity(product.getQuantity() + request.getQuantity());
    }

    @Override
    public AddressDto getWarehouseAddress() {
        log.info("Warehouse address: {}", CURRENT_ADDRESS);
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }

    private void checkProductAlreadyExists(UUID productId) {
        if (warehouseRepository.existsById(productId)) {
            throw new SpecifiedProductAlreadyInWarehouseException("Product with id: %s already exists"
                    .formatted(productId));
        }
    }

    private WarehouseItem checkProductNotFound(UUID productId) {
        return warehouseRepository.findById(productId)
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Product with id: %s not found"
                        .formatted(productId)));
    }
}