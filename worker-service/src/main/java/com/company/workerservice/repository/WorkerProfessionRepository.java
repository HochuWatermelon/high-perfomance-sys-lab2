package com.company.workerservice.repository;

import com.company.workerservice.model.entity.WorkerProfessionEntity;
import com.company.workerservice.model.entity.embedded.ProfessionWorkerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkerProfessionRepository extends JpaRepository<WorkerProfessionEntity, ProfessionWorkerId> {
    WorkerProfessionEntity findByWorkerId(UUID workerId);
}
