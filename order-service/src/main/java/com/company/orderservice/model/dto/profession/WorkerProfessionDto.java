package com.company.orderservice.model.dto.profession;

import lombok.Data;

import java.util.UUID;

@Data
public class WorkerProfessionDto {
    private UUID workerId;
    private String professionName;
}
