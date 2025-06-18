package com.vcarrin87.jdbc_example.models;

import java.util.Collection;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Products {

    private int productId;

    private String name;

    @Nullable
    private String description;

    private Double price;

    @Nullable
    private Collection<OrderItems> orderItems;
}
