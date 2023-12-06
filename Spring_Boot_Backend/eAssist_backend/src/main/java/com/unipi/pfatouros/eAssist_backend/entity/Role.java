package com.unipi.pfatouros.eAssist_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuppressWarnings("JpaDataSourceORMInspection")
public class Role {

    // This class corresponds to users' roles

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private RoleEnum name; // The name of the role (e.g. admin, manager, employee)

    // Constructor
    public Role(RoleEnum name) {
        this.name = name;
    }
}
