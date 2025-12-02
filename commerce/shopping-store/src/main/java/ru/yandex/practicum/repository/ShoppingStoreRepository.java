package ru.yandex.practicum.repository;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.model.Product;

public interface ShoppingStoreRepository extends JpaRepository<Product, UUID> {

    Page<Product> findAllByProductCategory(ProductCategory category, Pageable pageable);
}