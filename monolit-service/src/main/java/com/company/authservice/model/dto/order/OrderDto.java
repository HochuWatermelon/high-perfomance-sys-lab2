package com.company.authservice.model.dto.order;

import lombok.Data;
import com.company.authservice.model.enums.OrderStatusType;

import java.time.Instant;
import java.util.UUID;

@Data
public class OrderDto {
    private UUID id;
    private String customerFirstName;
    private String customerSecondName;
    private OrderStatusType orderStatusType;
    private Instant createdTime;
    private Integer cost;
    private String description;

    @Override
    public String toString() {
        return "ID заказа: " + id + "\nСтатус заказа: " + orderStatusType + "\nДата создания: " + createdTime + "\nЗаказчик: " + customerSecondName + " " + customerFirstName + "\nСтоимость: " + cost + "\nОписание: " + description;
    }
}
