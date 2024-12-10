package com.company.workerservice.repository;

import com.company.workerservice.model.entity.ProfessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfessionRepository extends JpaRepository<ProfessionEntity, UUID> {
    Optional<ProfessionEntity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
