package com.vcarrin87.jdbc_example.models;

import java.util.Date;
import java.util.List;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Orders {
    
    private int orderId;

    private int customerId;

    private String orderStatus;

    @Nullable
    private Date deliveryDate; 

    @Nullable
    private List<OrderItems> orderItems;

    @Nullable
    private List<Payments> payments;
}
