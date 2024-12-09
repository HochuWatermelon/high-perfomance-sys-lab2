package com.company.orderservice.model.dto.pageable;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PageableGetDto {
    private int page;
    @Min(value = 1, message = "Вы должны запрашивать, как минимум 1 запись.")
    @Max(value = 50, message = "Количество записей на странице не может превышать 50.")
    private int size;
}
