package com.company.orderservice.model.dto.order;

import lombok.Data;
import com.company.orderservice.model.enums.OrderStatusType;

import java.time.Instant;
import java.util.UUID;

@Data
public class OrderDto {
    private UUID id;
    private String customerFullName;
    private OrderStatusType orderStatusType;
    private Instant createdTime;
    private Integer cost;
    private String description;

    @Override
    public String toString() {
        return "ID заказа: " + id + "\nСтатус заказа: " + orderStatusType + "\nДата создания: " + createdTime + "\nЗаказчик: " + customerFullName + "\nСтоимость: " + cost + "\nОписание: " + description;
    }
}
