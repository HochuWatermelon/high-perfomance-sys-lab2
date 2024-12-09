package com.company.workservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "work")
public class WorkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "work_object", nullable = false)
    private String workObject;

    @Column(name = "required_edits")
    private String requiredEdits;

    @Column(name = "accepted_by_customer")
    private Boolean acceptedByCustomer = false;

}
