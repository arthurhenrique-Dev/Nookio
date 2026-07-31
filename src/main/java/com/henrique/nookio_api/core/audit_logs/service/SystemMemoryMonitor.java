package com.henrique.nookio_api.core.audit_logs.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SystemMemoryMonitor {

    @Getter
    @Value("${audit.memory.threshold:30.0}")
    private double memoryThresholdPercent;

    /**
     * Returns true if JVM Heap Memory usage exceeds the configured threshold percentage.
     */
    public boolean isMemoryUsageAboveThreshold() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        double usedPercentage = ((double) usedMemory / maxMemory) * 100;

        if (usedPercentage > memoryThresholdPercent) {
            log.warn("High JVM memory usage: {}%. Threshold limit of {}% reached. Routing audit logs directly to local JPA storage.",
                    String.format("%.2f", usedPercentage), memoryThresholdPercent);
            return true;
        }

        return false;
    }
}
