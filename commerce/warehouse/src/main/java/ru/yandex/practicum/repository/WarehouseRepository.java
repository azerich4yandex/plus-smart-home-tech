package ru.yandex.practicum.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.WarehouseItem;

public interface WarehouseRepository extends JpaRepository<WarehouseItem, UUID> {

}