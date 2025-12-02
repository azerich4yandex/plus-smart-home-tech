package ru.yandex.practicum.client;

import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.dto.shoppingstore.ProductDto;
import ru.yandex.practicum.enums.QuantityState;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {

    @GetMapping
    Page<ProductDto> getShoppingStoreProducts(@RequestParam String category,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "1") int size,
                                              @RequestParam(defaultValue = "") List<String> sort);

    @PutMapping
    ProductDto createProduct(@RequestBody ProductDto productDto);

    @PostMapping
    ProductDto updateProduct(@RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    Boolean deleteProduct(@RequestBody UUID id);

    @PostMapping("/quantityState")
    Boolean updateQuantityState(@RequestParam UUID productId, @RequestParam QuantityState quantityState);

    @GetMapping("/{productId}")
    ProductDto getProductById(@PathVariable UUID productId);
}