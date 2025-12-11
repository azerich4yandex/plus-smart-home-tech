package ru.yandex.practicum.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.OrderBooking;

public interface OrderBookingRepository extends JpaRepository<OrderBooking, UUID> {

}