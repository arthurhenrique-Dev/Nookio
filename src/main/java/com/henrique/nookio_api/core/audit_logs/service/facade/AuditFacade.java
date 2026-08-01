package com.henrique.nookio_api.core.audit_logs.service.facade;

import com.henrique.nookio_api.core.audit_logs.model.AuditLogData;
import com.henrique.nookio_api.core.audit_logs.service.SystemMemoryMonitor;
import com.henrique.nookio_api.core.audit_logs.service.strategies.contract.AuditStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditFacade {

    private final Map<String, AuditStrategy> strategies;
    private final SystemMemoryMonitor memoryMonitor;

    public void process(AuditLogData data) {
        if (memoryMonitor.isMemoryUsageAboveThreshold()) {
            log.info("JVM memory above {}%. Executing JPA fallback strategy.", 
                    memoryMonitor.getMemoryThresholdPercent());
            strategies.get("databaseLogDispatch").handle(data);
            return;
        }

        try {
            strategies.get("directLogDispatch").handle(data);
        } catch (Exception e) {
            log.warn("Failed to dispatch log to Analytics service. Executing JPA fallback strategy.", e);
            strategies.get("databaseLogDispatch").handle(data);
        }
    }
}
