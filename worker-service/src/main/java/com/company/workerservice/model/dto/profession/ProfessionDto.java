package com.company.workerservice.model.dto.profession;

import lombok.Data;

import java.util.UUID;

@Data
public class ProfessionDto {
    private UUID id;
    private String name;
}
