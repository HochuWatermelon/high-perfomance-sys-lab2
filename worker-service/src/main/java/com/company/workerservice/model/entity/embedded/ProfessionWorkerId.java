package com.company.workerservice.model.entity.embedded;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionWorkerId implements Serializable {
    @Column(name="worker_id")
    private UUID workerId;
    @Column(name="profession_id")
    private UUID professionId;
}
