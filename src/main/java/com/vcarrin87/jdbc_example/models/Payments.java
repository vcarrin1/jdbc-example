package com.vcarrin87.jdbc_example.models;

import java.sql.Timestamp;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Payments {

    private int paymentId;

    private int orderId;

    private double amount;

    @Nullable
    private Timestamp paymentDate;

    @Nullable
    private String paymentMethod;
}
