package com.unipi.pfatouros.eAssist_backend.repository;

import com.unipi.pfatouros.eAssist_backend.entity.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableRepository extends JpaRepository<TableEntity, Long> {

    // Contains the basic CRUD operations for TableEntity entity

    boolean existsByName(String name);

    Optional<TableEntity> findByName(String name);
}
