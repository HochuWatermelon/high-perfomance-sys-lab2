package com.company.monolitservice.model.entity;


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
@Table(name = "customer")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name", length = 32, nullable = false)
    private String firstName;

    @Column(name = "second_name", length = 32, nullable = false)
    private String secondName;

    @Column(name = "middle_name", length = 32, nullable = false)
    private String middleName;

    @Column(name = "registration_time", nullable = false)
    private Instant registrationTime = Instant.now();

    @Column(name = "number_of_paid_orders", nullable = false)
    private Integer numberOfPaidOrders = 0;

    public CustomerEntity(String firstName, String secondName, String middleName) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.middleName = middleName;
    }

}
