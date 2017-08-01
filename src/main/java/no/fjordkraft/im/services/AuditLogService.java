package no.fjordkraft.im.services;

import no.fjordkraft.im.model.AuditLog;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by miles on 7/10/2017.
 */
public interface AuditLogService {

    void saveAuditLog(Long actionOnId, String action, String msg, String logType, String invoiceNo);
    void saveAuditLog(String actionOnType, Long actionOnId, String action, String msg, String logType, String invoiceNo);
    Long getTotalAuditLog();
    AuditLog getAuditLogById(Long id);
    List<AuditLog> getAuditLogRecords(int page, int size, Timestamp fromTime, Timestamp toTime, String action,
                                      String actionOnType, Long actionOnId, String logType, String invoiceNo);
    Long getAuditLogRecordsCount(int page, int size, Timestamp fromTime, Timestamp toTime, String action,
                                      String actionOnType, Long actionOnId, String logType, String invoiceNo);
}
