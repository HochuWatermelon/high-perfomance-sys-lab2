package com.company.monolitservice.repository;


import com.company.monolitservice.model.entity.OrderStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatusEntity, UUID> {

    Optional<OrderStatusEntity> findByCode(String code);

    boolean existsByCode(String code);

}
