package com.company.monolitservice.model.dto.feedback;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.company.monolitservice.model.dto.customer.CustomerSelfDto;

@AllArgsConstructor
@Data
public class FeedbackPutDto {
    @Valid
    private CustomerSelfDto customer_self_dto;
    @NotBlank(message = "Поле не может быть null и должно содержать хотя бы 1 непробельный символ.")
    @Size(max = 128, message = "Не должно превышать 128 символов")
    private String feedback;
    @Min(value = 0, message = "Не может быть меньше 0.0")
    @Max(value = 5, message = "Не может быть больше 5.0")
    private Double score;
}
