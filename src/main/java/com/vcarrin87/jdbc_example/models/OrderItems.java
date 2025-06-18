package com.vcarrin87.jdbc_example.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItems {

    private int orderItemId;

    private int orderId;

    private int productId;

    private int quantity;

    private Double price;
}
