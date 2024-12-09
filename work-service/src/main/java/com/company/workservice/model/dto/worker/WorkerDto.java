package com.company.workservice.model.dto.worker;

import lombok.Data;

import java.util.UUID;

@Data
public class WorkerDto {
    private UUID id;
    private String fullName;
    private Integer numberOfCompletedOrders;

    @Override
    public String toString() {
        return "ID работника: " + id + "\nФИО: " + fullName + "\nКол-во выполненных заказов: " + numberOfCompletedOrders;
    }
}
