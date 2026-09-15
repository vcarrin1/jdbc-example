package com.vcarrin87.jdbc_example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vcarrin87.jdbc_example.models.Payments;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Integer> {
}
