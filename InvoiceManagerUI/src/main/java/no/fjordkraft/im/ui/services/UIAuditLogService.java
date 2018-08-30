package no.fjordkraft.im.ui.services;

import no.fjordkraft.im.domain.RestAuditLog;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by miles on 8/10/2017.
 */
public interface UIAuditLogService {

    List<RestAuditLog> getAuditLogRecords(int page, int size, Timestamp fromTime, Timestamp toTime, String action,
                                          String actionOnType, String logType, String invoiceNo, String customerID, String accountNumber,String legalPartClass);
    Long getAuditLogRecordsCount(Timestamp fromTime, Timestamp toTime, String action,
                                 String actionOnType, String logType, String invoiceNo, String customerID, String accountNumber,String legalPartClass);
}
