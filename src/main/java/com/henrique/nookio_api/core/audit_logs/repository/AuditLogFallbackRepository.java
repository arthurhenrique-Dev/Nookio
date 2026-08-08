package com.henrique.nookio_api.core.audit_logs.repository;

import com.henrique.nookio_api.core.audit_logs.model.AuditLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogFallbackRepository extends JpaRepository<AuditLogEntity, Integer> {

    Page<AuditLogEntity> findAllByOrderByAuditLogDataTimestampAsc(PageRequest of);

    @Query(value = "TRUNCATE TABLE local_logs", nativeQuery = true)
    void truncate();
}
