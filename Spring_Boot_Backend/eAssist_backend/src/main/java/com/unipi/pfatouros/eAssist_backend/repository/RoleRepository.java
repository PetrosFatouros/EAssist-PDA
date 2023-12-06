package com.unipi.pfatouros.eAssist_backend.repository;

import com.unipi.pfatouros.eAssist_backend.entity.Role;
import com.unipi.pfatouros.eAssist_backend.entity.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // Contains the basic CRUD operations for Role entity

    Optional<Role> findByName(RoleEnum role);

    boolean existsByName(RoleEnum name);
}
