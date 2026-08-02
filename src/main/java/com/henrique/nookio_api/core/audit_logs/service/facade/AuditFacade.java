package com.henrique.nookio_api.core.audit_logs.service.facade;

import com.henrique.nookio_api.core.audit_logs.model.AuditLogData;
import com.henrique.nookio_api.core.audit_logs.service.strategies.intefaces.AuditStrategy;
import com.henrique.nookio_api.core.health_monitor.ApplicationStress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditFacade {

    private final Map<String, AuditStrategy> strategies;
    private final ApplicationStress stress;

    public void process(AuditLogData data) {
        if (!stress.isStressed()) strategies.get("directLogDispatch").handle(data);
        strategies.get("databaseLogDispatch").handle(data);
    }
}
