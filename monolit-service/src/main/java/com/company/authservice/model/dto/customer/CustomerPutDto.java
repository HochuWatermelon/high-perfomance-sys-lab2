package com.company.authservice.model.dto.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CustomerPutDto {
    @NotBlank(message = "Поле не может быть null и должно содержать хотя бы 1 непробельный символ.")
    @Size(max = 32, message = "Не должно превышать 32 символа")
    private String first_name;
    @NotBlank (message = "Поле не может быть null и должно содержать хотя бы 1 непробельный символ.")
    @Size(max = 32, message = "Не должно превышать 32 символа")
    private String second_name;
    @NotBlank (message = "Поле не может быть null и должно содержать хотя бы 1 непробельный символ.")
    @Size(max = 32, message = "Не должно превышать 32 символа")
    private String middle_name;
}
