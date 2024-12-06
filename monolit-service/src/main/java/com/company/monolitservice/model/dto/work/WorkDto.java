package com.company.monolitservice.model.dto.work;

import lombok.Data;

@Data
public class WorkDto {
    private String workObject;
    private String requiredEdits;
    private Boolean acceptedByCustomer;

    @Override
    public String toString() {
        return "Выполненная работа: " + workObject + "\nНеобходимые правки: " + requiredEdits + "\nПринята заказчиком: " + acceptedByCustomer;
    }
}
