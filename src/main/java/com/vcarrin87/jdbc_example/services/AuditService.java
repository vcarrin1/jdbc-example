package com.vcarrin87.jdbc_example.services;

import com.vcarrin87.jdbc_example.models.AuditRecord;
import com.vcarrin87.jdbc_example.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository auditRepository;

    public void createAuditRecord(
            String action,
            String resource,
            String resourceId,
            String status,
            String details,
            String userId) {

        AuditRecord auditRecord = AuditRecord.builder()
                .action(action)
                .resource(resource)
                .resourceId(resourceId)
                .status(status)
                .details(details)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();

        auditRepository.save(auditRecord);
    }
}
