package com.vcarrin87.jdbc_example.models;

import java.util.List;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Customer {

    private int customerId;

    private String name;

    private String email;

    @Nullable
    private String address;

    @Nullable
    private List<Orders> orders;
}
