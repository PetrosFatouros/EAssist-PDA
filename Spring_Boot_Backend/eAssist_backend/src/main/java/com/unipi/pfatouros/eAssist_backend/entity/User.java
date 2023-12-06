package com.unipi.pfatouros.eAssist_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuppressWarnings("JpaDataSourceORMInspection")
public class User {

    // This class corresponds to the application users

    @Id
    @Column(length = 20)
    private String username; // Primary key

    @Column(length = 50, nullable = false, unique = true)
    private String email; // Email of the user

    @Column(length = 120, nullable = false)
    @JsonIgnore
    private String password; // Password of the user

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>(); // The roles of the user (each user can have more than one role)
}
