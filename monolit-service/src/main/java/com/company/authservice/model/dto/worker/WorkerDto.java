package com.company.authservice.model.dto.worker;

import lombok.Data;

import java.util.UUID;

@Data
public class WorkerDto {
    private UUID id;
    private String firstName;
    private String secondName;
    private String middleName;
    private Integer numberOfCompletedOrders;
    private Double rating;

    @Override
    public String toString() {
        return "ID работника: " + id + "\nФИО: " + secondName + " " + firstName + " " + middleName  + "\nКол-во выполненных заказов: " + numberOfCompletedOrders + "\nРейтинг: " + rating;
    }
}
