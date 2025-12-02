package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.ProductState;
import ru.yandex.practicum.enums.QuantityState;

@Entity
@Table(name = "products", schema = "shopping_store")
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    UUID id;

    @Column(name = "product_name")
    @NotBlank
    String productName;

    @NotBlank
    @Column(name = "description")
    String description;

    @Column(name = "image_src")
    String imageSrc;

    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_state")
    @NotNull
    QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_state")
    @NotNull
    ProductState productState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category")
    ProductCategory productCategory;

    @Column(name = "price")
    @NotNull
    Double price;
}
