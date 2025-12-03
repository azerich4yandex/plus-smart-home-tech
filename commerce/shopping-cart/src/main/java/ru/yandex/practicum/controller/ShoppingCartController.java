package ru.yandex.practicum.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.client.ShoppingCartOperations;
import ru.yandex.practicum.dto.shoppingcart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.service.ShoppingCartService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController implements ShoppingCartOperations {

    private final ShoppingCartService shoppingCartService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto getShoppingCart(@RequestParam(name = "username") String userName) {
        log.info("Getting shopping cart for user {}", userName);
        return shoppingCartService.getUserShoppingCart(userName);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto addProductToShoppingCart(@RequestParam(name = "username") String userName,
                                                    @RequestBody Map<UUID, Long> products) {
        log.info("Adding products {} to shopping cart for user {}", products, userName);
        return shoppingCartService.addProductsToShoppingCart(userName, products);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void deactivateShoppingCart(@RequestParam(name = "username") String userName) {
        log.info("Deactivating shopping cart for user {}", userName);
        shoppingCartService.deactivateShoppingCart(userName);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto removeProductsFromShoppingCart(@RequestParam(name = "username") String userName,
                                                          @RequestBody List<UUID> productList) {
        log.info("Removing products {} from shopping cart for user {}", productList, userName);
        return shoppingCartService.removeProductsFromShoppingCart(userName, productList);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto changeProductQuantity(@RequestParam(name = "username") String userName,
                                                 @RequestBody ChangeProductQuantityRequest request) {
        log.info("Changing quantity of products {} in shopping cart for user {}", request, userName);
        return shoppingCartService.changeQuantityOfProductsInShoppingCart(userName, request);
    }
}
