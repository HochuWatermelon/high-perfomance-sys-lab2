package com.company.authservice.repository;


import com.company.authservice.model.entity.WorkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkRepository extends JpaRepository<WorkEntity, UUID> {

}
