package com.company.monolitservice.model.dto.orderstatus;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.company.monolitservice.model.enums.OrderStatusType;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderStatusPostDto {
    @NotBlank(message = "Поле не может быть null и должно содержать хотя бы 1 непробельный символ.")
    private String code;
    @NotNull(message = "Не должно быть null.")
    private OrderStatusType order_status_type;
}
