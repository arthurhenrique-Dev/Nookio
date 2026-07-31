package com.henrique.nookio_api.core.audit_logs.service;

import com.henrique.nookio_api.core.audit_logs.model.AuditLogData;
import com.henrique.nookio_api.core.audit_logs.model.AuditLogEntity;
import com.henrique.nookio_api.core.audit_logs.repository.AuditLogFallbackRepository;
import com.henrique.nookio_api.core.integrations.analytics.config.AnalyticConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogRetryScheduler {

    private static final int BATCH_CHUNK_SIZE = 80;

    private final AuditLogFallbackRepository fallbackRepository;
    private final AnalyticConfig analyticConfig;
    private final SystemMemoryMonitor memoryMonitor;

    @Scheduled(fixedDelay = 15000)
    public void processPendingLogsInBatch() {
        while (!memoryMonitor.isMemoryUsageAboveThreshold()) {
            List<AuditLogEntity> pendingEntities = fallbackRepository
                    .findAll(PageRequest.of(0, BATCH_CHUNK_SIZE))
                    .getContent();

            if (pendingEntities.isEmpty()) {
                break;
            }

            List<AuditLogData> dataList = pendingEntities.stream()
                    .map(AuditLogEntity::getAuditLogData)
                    .toList();

            log.info("Sending batch chunk of {} pending audit logs to Analytics...", dataList.size());

            try {
                // Envia o lote e verifica se o Analytics respondeu com status 2xx (Sucesso)
                boolean isAccepted = analyticConfig.senderAuditLogs(dataList);

                if (isAccepted) {
                    // APAGA DO BANCO APENAS SE TIVER SIDO ENVIADO E ACEITO (2xx SUCCESSFUL)
                    fallbackRepository.deleteAll(pendingEntities);
                    log.info("Successfully dispatched chunk of {} logs and purged from local database.", dataList.size());
                } else {
                    log.warn("Analytics service responded with non-2xx status. Retaining chunk of {} logs in database for next retry.", dataList.size());
                    break; // Interrompe para tentar novamente no próximo ciclo do scheduler
                }
            } catch (Exception e) {
                log.error("Failed to dispatch chunk to Analytics. Retaining logs in local database for next retry cycle.", e);
                break;
            }
        }
    }
}
