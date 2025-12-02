package ru.yandex.practicum.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {

    Optional<ShoppingCart> findShoppingCartByUserName(String userName);

    Optional<ShoppingCart> findShoppingCartByUserNameAndIsActive(String userName, boolean isActive);

}