package com.unipi.pfatouros.eAssist_backend.repository;

import com.unipi.pfatouros.eAssist_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    // Contains the basic CRUD operations for User entity

    boolean existsByEmail(String email);
}
