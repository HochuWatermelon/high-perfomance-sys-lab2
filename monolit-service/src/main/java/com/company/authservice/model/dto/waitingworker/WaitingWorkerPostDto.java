package com.company.authservice.model.dto.waitingworker;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class WaitingWorkerPostDto {
    @NotNull(message = "Не может быть null.")
    private UUID worker_id;
}
