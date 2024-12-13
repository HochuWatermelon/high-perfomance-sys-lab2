package com.company.orderservice.model.dto.profession;

import lombok.Data;

import java.util.UUID;

@Data
public class ProfessionDto {
    private UUID id;
    private String name;
}
