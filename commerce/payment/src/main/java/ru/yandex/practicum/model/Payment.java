package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.dto.payment.PaymentState;

@Entity
@Table(name = "payment")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    UUID id;

    @Column(name = "order_id")
    @NotNull
    UUID orderId;

    @Column(name = "products_total")
    double productsTotal;

    @Column(name = "delivery_total")
    double deliveryTotal;

    @Column(name = "total_payment")
    double totalPayment;

    @Column(name = "fee_total")
    double feeTotal;

    @Column(name = "payment_state")
    @Enumerated(EnumType.STRING)
    PaymentState paymentState;
}