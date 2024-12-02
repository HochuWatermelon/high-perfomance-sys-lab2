package com.company.authservice.model.dto.customer;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class CustomerSelfDto {
    @NotNull(message = "Поле не может быть null.")
    private UUID customer_id;
    @NotNull(message = "Поле не может быть null.")
    private UUID order_id;
}
