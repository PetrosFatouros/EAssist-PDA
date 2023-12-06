package com.unipi.pfatouros.eAssist_backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tables")
@NoArgsConstructor
@Getter
@Setter
@SuppressWarnings("JpaDataSourceORMInspection")
public class TableEntity {

    // This class corresponds to the physical tables of the business

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key

    @Column(length = 20, nullable = false, unique = true)
    private String name; // Name of the table

    @Column(nullable = false)
    private Boolean is_available; // True if table is available for new customers, false otherwise

    public TableEntity(String name, Boolean is_available) {
        this.name = name;
        this.is_available = is_available;
    }
}
