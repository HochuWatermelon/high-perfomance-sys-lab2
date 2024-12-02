package com.company.authservice.model.dto.work;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import com.company.authservice.model.dto.customer.CustomerSelfDto;

@Data
public class WaitingApprovalWorkPutDto {
    @Valid
    private CustomerSelfDto customer_self_dto;
    @NotBlank(message = "Поле не может быть null и должно содержать хотя бы 1 непробельный символ.")
    private String required_edits;
}
