package com.company.authservice.repository;


import com.company.authservice.model.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    List<OrderEntity> findByOrderStatusCodeAndProfessionId(String code, UUID professionId);

    Page<OrderEntity> findByWorkerIdAndWorkNotNull(UUID workerId, Pageable pageable);
    List<OrderEntity> findByWorkerIdAndWorkNotNull(UUID workerId);

}
