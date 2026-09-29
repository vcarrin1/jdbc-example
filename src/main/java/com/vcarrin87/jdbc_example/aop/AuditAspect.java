package com.vcarrin87.jdbc_example.aop;

import com.vcarrin87.jdbc_example.annotation.Audit;
import com.vcarrin87.jdbc_example.security.SecurityUtil;
import com.vcarrin87.jdbc_example.services.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditService auditService;

    @Around("@annotation(audit)")
    public Object audit(ProceedingJoinPoint joinPoint, Audit audit) throws Throwable {

        String action = audit.action();
        String resource = audit.resource();

        Object[] args = joinPoint.getArgs();

        String resourceId = String.valueOf(args[0]);
        String details = String.valueOf(args[1]);

        try {
            Object result = joinPoint.proceed();

            auditService.createAuditRecord(
                    action,
                    resource,
                    resourceId,
                    "SUCCESS",
                    details,
                    SecurityUtil.getCurrentUser()
            );

            return result;

        } catch (Exception ex) {

            auditService.createAuditRecord(
                    action,
                    resource,
                    resourceId,
                    "FAILURE",
                    ex.getMessage(),
                    SecurityUtil.getCurrentUser()
            );

            throw ex;
        }
    }
}
