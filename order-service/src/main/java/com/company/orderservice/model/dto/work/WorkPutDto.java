package com.company.orderservice.model.dto.work;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;


@Data
public class WorkPutDto {
    @NotNull
    private UUID order_id;
    @NotBlank(message = "Поле не может быть null и должно содержать хотя бы 1 непробельный символ.")
    private String work_object;
}
