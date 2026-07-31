package com.henrique.nookio_api.core.audit_logs.service.strategies.contract;

import com.henrique.nookio_api.core.audit_logs.model.AuditLogData;

public interface AuditStrategy {
    void handle(AuditLogData data);
}
