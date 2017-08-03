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
    Long getTotalAuditLog();
    AuditLog getAuditLogById(Long id);
    List<RestAuditLog> getAuditLogRecords(int page, int size, Timestamp fromTime, Timestamp toTime, String action,
                                      String actionOnType, String logType, String invoiceNo, String customerID, String accountNumber);
    Long getAuditLogRecordsCount(Timestamp fromTime, Timestamp toTime, String action,
                                      String actionOnType, String logType, String invoiceNo, String customerID, String accountNumber);
}
