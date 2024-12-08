package com.company.authservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.company.authservice.models.enums.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private UUID id;

    @Column("full_name")
    private String fullName;
    @Column("password")
    private String password;
    @Column("confirmed")
    private boolean confirmed = false;
    @Column("blocked")
    private boolean blocked = false;
    @Column("role")
    private Role role = Role.CUSTOMER;

}