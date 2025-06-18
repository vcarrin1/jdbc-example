package com.vcarrin87.jdbc_example.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Inventory {
  
    private int productId;

    private int stockLevel;
}
