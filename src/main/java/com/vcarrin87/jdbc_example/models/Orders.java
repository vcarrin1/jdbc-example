package com.vcarrin87.jdbc_example.models;

import java.util.Collection;
import java.util.Date;

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
    private Collection<Payments> payments;
}
