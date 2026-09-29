package com.vcarrin87.jdbc_example.repository;

import com.vcarrin87.jdbc_example.models.AuditRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<AuditRecord, Long> {
}
