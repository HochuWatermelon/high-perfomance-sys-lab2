package com.company.authservice.repository;

import com.company.authservice.model.entity.WorkerProfessionEntity;
import com.company.authservice.model.entity.embedded.ProfessionWorkerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkerProfessionRepository extends JpaRepository<WorkerProfessionEntity, ProfessionWorkerId> {
    WorkerProfessionEntity findByWorkerId(UUID workerId);

    Boolean existsByWorkerIdAndProfessionId(UUID workerId,UUID professionId);
}
