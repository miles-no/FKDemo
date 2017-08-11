package no.fjordkraft.im.services;

import no.fjordkraft.im.domain.RestAuditLog;
import no.fjordkraft.im.model.AuditLog;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by miles on 7/10/2017.
 */
public interface AuditLogService {

    void saveAuditLog(Long actionOnId, String action, String msg, String logType);
    void saveAuditLog(String actionOnType, Long actionOnId, String action, String msg, String logType);
}
