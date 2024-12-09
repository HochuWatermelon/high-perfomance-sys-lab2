package com.company.workservice.model.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatusType implements IDescriptiveEnum<OrderStatusType> {
    NEW("NEW"),
    IN_WORK("IN_WORK"),
    WAITING_APPROVAL("WAITING_APPROVAL"),
    FINISHED("FINISHED"),
    PAID("PAID");

    private final String description;
}
