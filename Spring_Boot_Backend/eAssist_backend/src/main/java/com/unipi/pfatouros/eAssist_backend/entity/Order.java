package com.unipi.pfatouros.eAssist_backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
@Setter
@SuppressWarnings("JpaDataSourceORMInspection")
public class Order {

    // This class corresponds to customers' orders

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key

    @OneToOne
    @JoinColumn(name = "table_id", referencedColumnName = "id")
    private TableEntity table; // Associated table

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OrderEnum status; // Current status of the order (e.g. pending, active, etc)

    @Column(nullable = false)
    private Date date; // Date the order was created

    // Constructor
    public Order(TableEntity table, OrderEnum status, Date date) {
        this.table = table;
        this.status = status;
        this.date = date;
    }
}
