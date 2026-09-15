package com.vcarrin87.jdbc_example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vcarrin87.jdbc_example.models.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    // Saving a transient Orders entity assigns the generated order_id back onto the entity
    default int saveWithGeneratedKey(Orders order) {
        return save(order).getOrderId();
    }
}
