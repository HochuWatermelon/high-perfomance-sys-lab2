package com.company.monolitservice.model.dto.waitingworker;

import jakarta.validation.Valid;
import lombok.Data;
import com.company.monolitservice.model.dto.customer.CustomerSelfDto;
import com.company.monolitservice.model.dto.pageable.PageableGetDto;

@Data
public class WaitingWorkerGetDto {
    @Valid
    private CustomerSelfDto customer_self_dto;
    @Valid
    private PageableGetDto pageable_get_dto;
}
