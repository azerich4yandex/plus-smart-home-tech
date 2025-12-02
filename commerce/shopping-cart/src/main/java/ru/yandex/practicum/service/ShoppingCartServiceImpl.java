package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.dto.shoppingcart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.exceptions.NotAuthorizedUserException;
import ru.yandex.practicum.exceptions.NotFoundShoppingCartException;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.repository.ShoppingCartRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    public ShoppingCartDto getUserShoppingCart(String userName) {
        ShoppingCart shoppingCart = checkAndGetShoppingCart(userName);
        log.info("Get shopping cart {} for user {}", shoppingCart, userName);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartDto addProductsToShoppingCart(String userName, Map<UUID, Long> products) {
        checkUser(userName);
        ShoppingCart shoppingCart = shoppingCartMapper.toEntity(userName, products, true);
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
        log.info("Add products {} to shopping cart {} for user {}", products, shoppingCartDto, userName);
        BookedProductsDto bookedProductsDto = warehouseClient.checkQuantity(shoppingCartDto);
        log.info("Booked products {} for user {}", bookedProductsDto, userName);
        return shoppingCartDto;
    }

    @Transactional
    @Override
    public void deactivateShoppingCart(String userName) {
        ShoppingCart shoppingCart = checkAndGetShoppingCart(userName);
        shoppingCart.setIsActive(false);
    }

    @Transactional
    @Override
    public ShoppingCartDto removeProductsFromShoppingCart(String userName, List<UUID> productIdList) {
        ShoppingCart shoppingCart = checkAndGetShoppingCart(userName);
        Set<UUID> cartProducts = getProductIdsFromShoppingCart(shoppingCart, productIdList);
        productIdList.stream()
                .filter(cartProducts::contains)
                .forEach(productId -> shoppingCart.getProducts().remove(productId));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartDto changeQuantityOfProductsInShoppingCart(String userName,
                                                                  ChangeProductQuantityRequest request) {
        ShoppingCart shoppingCart = checkAndGetShoppingCart(userName);
        shoppingCart.getProducts().put(request.getProductId(), request.getNewQuantity());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    private void checkUser(String userName) {
        if (userName == null) {
            log.error("User name must be not empty");
            throw new NotAuthorizedUserException("User name must be not empty");
        }
    }

    private ShoppingCart checkAndGetShoppingCart(String userName) {
        checkUser(userName);
        return shoppingCartRepository
                .findShoppingCartByUserName(userName)
                .orElseThrow(() -> new NotFoundShoppingCartException("Shopping cart for user %s not found"
                        .formatted(userName)));
    }

    private Set<UUID> getProductIdsFromShoppingCart(ShoppingCart shoppingCart, Collection<UUID> productIdList) {
        Set<UUID> cartProducts = shoppingCart.getProducts().keySet();
        if (!cartProducts.containsAll(productIdList)) {
            throw new NotFoundShoppingCartException("Product not found in shopping cart");
        }
        return cartProducts;
    }
}