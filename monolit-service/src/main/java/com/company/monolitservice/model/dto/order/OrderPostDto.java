package com.company.monolitservice.model.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderPostDto {
    @NotBlank(message = "Поле не может быть null и должно содержать хотя бы 1 непробельный символ.")
    @Size(max = 32, message = "Не должно превышать 32 символов")
    private String profession_name;
    @NotNull(message = "Не может быть null.")
    private Integer cost;
    @NotBlank(message = "Поле не может быть null и должно содержать хотя бы 1 непробельный символ.")
    @Size(max = 512, message = "Не должно превышать 512 символов")
    private String description;
}
