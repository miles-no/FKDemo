package no.fjordkraft.im.ui.services.impl;

import no.fjordkraft.im.domain.RestAuditLog;
import no.fjordkraft.im.model.AuditLog;
import no.fjordkraft.im.repository.AuditLogDetailRepository;
import no.fjordkraft.im.ui.services.UIAuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by miles on 8/10/2017.
 */
@Service
public class UIAuditLogServiceImpl implements UIAuditLogService {

    @Autowired
    AuditLogDetailRepository auditLogDetailRepository;

    @Override
    public List<RestAuditLog> getAuditLogRecords(int page, int size, Timestamp fromTime, Timestamp toTime, String action, String actionOnType, String logType,
                                                 String invoiceNo, String customerID, String accountNumber) {
        List<RestAuditLog> restAuditLogs = new ArrayList<>();
        RestAuditLog restAuditLog;
        List<AuditLog> auditLogList = auditLogDetailRepository.getDetails(page, size, fromTime, toTime, action, actionOnType, logType, invoiceNo, customerID, accountNumber);

        for(AuditLog auditLog:auditLogList) {
            restAuditLog = new RestAuditLog();
            restAuditLog.setActionOnId(auditLog.getActionOnId());
            restAuditLog.setActionOnType(auditLog.getActionOnType());
            restAuditLog.setAction(auditLog.getAction());
            restAuditLog.setLogType(auditLog.getLogType());
            restAuditLog.setDateTime(auditLog.getDateTime());
            restAuditLog.setUserName(auditLog.getUserName());
            restAuditLog.setMsg(auditLog.getMsg());
            if(null != auditLog.getStatement()) {
                restAuditLog.setAccountNumber(auditLog.getStatement().getAccountNumber());
                restAuditLog.setCustomerId(auditLog.getStatement().getCustomerId());
                restAuditLog.setInvoiceNo(auditLog.getStatement().getInvoiceNumber());
            }
            restAuditLogs.add(restAuditLog);
        }
        return restAuditLogs;
    }

    @Override
    public Long getAuditLogRecordsCount(Timestamp fromTime, Timestamp toTime, String action, String actionOnType, String logType,
                                        String invoiceNo, String customerID, String accountNumber) {
        return auditLogDetailRepository.getCount(fromTime, toTime, action, actionOnType, logType, invoiceNo, customerID, accountNumber);
    }
}
