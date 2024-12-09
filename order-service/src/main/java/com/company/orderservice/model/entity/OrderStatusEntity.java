package com.company.orderservice.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.company.orderservice.model.enums.OrderStatusType;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "order_status")
public class OrderStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(name = "order_status_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatusType orderStatusType;

    public OrderStatusEntity(String code, OrderStatusType orderStatusType) {
        this.code = code;
        this.orderStatusType = orderStatusType;
    }

}
