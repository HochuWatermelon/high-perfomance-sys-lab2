package com.company.orderservice.model.dto.work;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WaitingApprovalWorkPutDto {
    @NotBlank(message = "Поле не может быть null и должно содержать хотя бы 1 непробельный символ.")
    private String required_edits;
}
