package com.company.authservice.repository;

import com.company.authservice.model.entity.FeedbackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, UUID> {
    Page<FeedbackEntity> findAllByWorkerId(UUID workerId, Pageable pageable);

    Optional<FeedbackEntity> findByOrderId(UUID orderId);
}
