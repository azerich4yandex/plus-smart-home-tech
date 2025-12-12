package ru.yandex.practicum.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

}