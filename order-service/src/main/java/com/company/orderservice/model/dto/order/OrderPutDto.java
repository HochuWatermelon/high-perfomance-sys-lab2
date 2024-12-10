package com.company.orderservice.model.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OrderPutDto {
    @NotNull(message = "Не может быть null.")
    private int cost;
    @NotBlank(message = "Поле не может быть null и должно содержать хотя бы 1 непробельный символ.")
    @Size(max = 512, message = "Не должно превышать 512 символов")
    private String description;
}
