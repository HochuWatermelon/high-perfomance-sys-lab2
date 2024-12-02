package com.company.authservice.model.enums;

public interface IDescriptiveEnum<T extends Enum<T> & IDescriptiveEnum<T>> {
    String getDescription();

    default String getCode() {
        return ((Enum<?>) this).name();
    }
}
