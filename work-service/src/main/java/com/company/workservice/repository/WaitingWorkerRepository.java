package com.company.workservice.repository;

import com.company.workservice.model.entity.WaitingWorkerEntity;
import com.company.workservice.model.entity.embedded.WaitingWorkerId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WaitingWorkerRepository extends JpaRepository<WaitingWorkerEntity, WaitingWorkerId> {
    List<WaitingWorkerEntity> findByOrderId(UUID orderId);
    List<WaitingWorkerEntity> findByOrderCustomerIdAndOrderId(UUID customerId, UUID orderId);
    Page<WaitingWorkerEntity> findAllByOrderCustomerIdAndOrderId(UUID customerId, UUID orderId, Pageable pageable);
    Optional<WaitingWorkerEntity> findByWorkerIdAndOrderIdAndOrderCustomerId(UUID workerId, UUID orderId, UUID customerId);
}
