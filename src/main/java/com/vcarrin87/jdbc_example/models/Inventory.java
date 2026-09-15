package com.vcarrin87.jdbc_example.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
public class Inventory {

    // Shared primary key with products (no auto-generation)
    @Id
    @Column(name = "product_id")
    private int productId;

    @Column(name = "stock_level", nullable = false)
    private int stockLevel;
}
