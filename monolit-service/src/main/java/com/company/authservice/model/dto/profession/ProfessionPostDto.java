package com.company.authservice.model.dto.profession;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfessionPostDto {
    @NotBlank(message = "Поле не может быть null и должно содержать хотя бы 1 непробельный символ.")
    @Size(max = 32, message = "Не должно превышать 32 символа")
    private String name;
}
