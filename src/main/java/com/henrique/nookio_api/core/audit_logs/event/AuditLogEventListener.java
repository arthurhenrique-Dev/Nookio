package com.henrique.nookio_api.core.audit_logs.event;

import com.henrique.nookio_api.core.audit_logs.service.facade.AuditFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditLogEventListener {

    private final AuditFacade facade;

    @Async
    @EventListener
    public void handleAuditLogEvent(AuditLogEvent event) {
        facade.process(event.getData());
    }
}
