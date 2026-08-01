package com.henrique.nookio_api.core.audit_logs.aspect;

import com.henrique.nookio_api.core.audit_logs.annotation.AuditLog;
import com.henrique.nookio_api.core.audit_logs.enums.Result;
import com.henrique.nookio_api.core.audit_logs.event.AuditLogEvent;
import com.henrique.nookio_api.core.audit_logs.model.AuditLogData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;
import java.util.function.Function;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final ApplicationEventPublisher eventPublisher;

    @Around("@annotation(auditLog)")
    public Object logAudit(ProceedingJoinPoint joinPoint, AuditLog auditLog) throws Throwable {
        String ip = extractFromRequestContext(
                attr -> {
                    HttpServletRequest req = attr.getRequest();
                    String xForwarded = req.getHeader("X-Forwarded-For");
                    return (xForwarded != null && !xForwarded.isBlank())
                            ? xForwarded.split(",")[0].trim()
                            : req.getRemoteAddr();
                },
                "SYSTEM_UNKNOWN"
        );

        Object result;

        try {
            result = joinPoint.proceed();
        } finally {

            Result auditResult = extractFromRequestContext(
                    attr -> Optional.ofNullable(attr.getResponse())
                            .map(HttpServletResponse::getStatus)
                            .map(Result::fromHttpStatusCode)
                            .orElse(Result.ERROR),
                    Result.ERROR
            );

            AuditLogData logData = AuditLogData.builder()
                    .ip(ip)
                    .resource(auditLog.resource())
                    .operation(auditLog.operation())
                    .result(auditResult.ordinal())
                    .build();

            eventPublisher.publishEvent(new AuditLogEvent(this, logData));
        }
            return result;
    }
    private <T> T extractFromRequestContext(Function<ServletRequestAttributes, T> extractor, T defaultValue) {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(extractor)
                .orElse(defaultValue);
    }
}
