package com.company.workservice.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "worker")
public class WorkerEntity {
    @Id
    private UUID id;

    @Column(name = "full_name", length = 64, nullable = false)
    private String fullName;

    @Column(name = "registration_time", nullable = false)
    private Instant registrationTime = Instant.now();

    @Column(name = "number_of_completed_orders", nullable = false)
    private Integer numberOfCompletedOrders = 0;



    public WorkerEntity(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0); // Используйте только id для hashCode
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerEntity that = (WorkerEntity) o;
        return id != null && id.equals(that.id);
    }
}
