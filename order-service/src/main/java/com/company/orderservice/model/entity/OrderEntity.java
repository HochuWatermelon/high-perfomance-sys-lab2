package com.company.orderservice.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "\"order\"")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "worker_id")
    private UUID workerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_id")
    private UUID customerId;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "work_id")
    private WorkEntity work;

    @Column(name = "profession_name")
    private String professionName;

    @ManyToOne
    private OrderStatusEntity orderStatus;

    @Column(name = "created_time", nullable = false)
    private Instant createdTime = Instant.now();

    @Column(name = "started_time")
    private Instant startedTime;

    @Column(name = "finished_time")
    private Instant finishedTime;

    @Column(nullable = false)
    private Integer cost;

    @Column(nullable = false, length = 512)
    private String description;

}
