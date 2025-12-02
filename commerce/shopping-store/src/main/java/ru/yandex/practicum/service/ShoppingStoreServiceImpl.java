package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.shoppingstore.ProductDto;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.ProductState;
import ru.yandex.practicum.enums.QuantityState;
import ru.yandex.practicum.exceptions.ProductNotFoundException;
import ru.yandex.practicum.mapper.ShoppingStoreMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ShoppingStoreRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private final ShoppingStoreRepository shoppingStoreRepository;
    private final ShoppingStoreMapper shoppingStoreMapper;

    @Override
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        log.info("Getting products by category {} and pageable {}", category, pageable);
        return shoppingStoreRepository.findAllByProductCategory(category, pageable)
                .map(shoppingStoreMapper::toDto);
    }

    @Transactional
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = shoppingStoreRepository.save(shoppingStoreMapper.toEntity(productDto));
        log.info("Product: {} by ProductDto: {} created", product, productDto);
        return shoppingStoreMapper.toDto(product);
    }

    @Transactional
    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        checkAndGetProduct(productDto.getProductId());
        Product product = shoppingStoreMapper.toEntity(productDto);
        log.info("Product: {} by ProductDto: {} updated", product, productDto);
        return shoppingStoreMapper.toDto(product);
    }

    @Transactional
    @Override
    public Boolean deleteProduct(UUID productId) {
        Product product = checkAndGetProduct(productId);
        log.info("Deactivate product: {} ", product);
        product.setProductState(ProductState.DEACTIVATE);
        return true;
    }

    @Transactional
    @Override
    public Boolean updateQuantityState(UUID productId, QuantityState quantityState) {
        Product product = checkAndGetProduct(productId);
        product.setQuantityState(quantityState);
        log.info("Product {} quantity state changed to {}", product, quantityState);
        return true;
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        log.info("Product with id {} requested", productId);
        return shoppingStoreMapper.toDto(checkAndGetProduct(productId));
    }

    private Product checkAndGetProduct(UUID productId) {
        return shoppingStoreRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id %s not found".formatted(productId)));
    }
}