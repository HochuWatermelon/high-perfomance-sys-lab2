package com.company.monolitservice.model.entity;


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

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private WorkerEntity worker;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "work_id")
    private WorkEntity work;

    @ManyToOne
    @JoinColumn(name = "profession_id")
    private ProfessionEntity profession;

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

    public OrderEntity(CustomerEntity customer, ProfessionEntity profession, Integer cost, String description) {
        this.customer = customer;
        this.profession = profession;
        this.cost = cost;
        this.description = description;
    }

}
