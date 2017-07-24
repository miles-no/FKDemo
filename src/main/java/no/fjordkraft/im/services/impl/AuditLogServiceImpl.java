package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.logging.AuditLogRecord;
import no.fjordkraft.im.model.AuditLog;
import no.fjordkraft.im.repository.AuditLogDetailRepository;
import no.fjordkraft.im.repository.AuditLogRepository;
import no.fjordkraft.im.services.AuditLogService;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by miles on 7/10/2017.
 */
@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    AuditLogRepository auditLogRepository;

    @Autowired
    AuditLogDetailRepository auditLogDetailRepository;

    @Override
    public void saveAuditLog(Long actionOnId, String action, String msg, String logType) {
        AuditLogRecord auditLogRecord = AuditLogRecord.logBuilder()
                .withActionOnType(IMConstants.STATEMENT)
                .withActionOnId(actionOnId)
                .withAction(action)
                .withmsg(msg)
                .withDateTime(new Timestamp(System.currentTimeMillis()))
                .withUsername(IMConstants.SYSTEM)
                .withLogType(logType)
                .build();

        AuditLog auditLog = new AuditLog();
        auditLog.setActionOnType(auditLogRecord.getActionOnType());
        auditLog.setActionOnId(auditLogRecord.getActionOnId());
        auditLog.setAction(auditLogRecord.getAction());
        auditLog.setUserName(auditLogRecord.getUsername());
        auditLog.setMsg(auditLogRecord.getMsg());
        auditLog.setDateTime(auditLogRecord.getDateTime());
        auditLog.setLogType(auditLogRecord.getLogType());

        auditLogRepository.saveAndFlush(auditLog);
    }

    @Override
    public void saveAuditLog(String actionOnType, Long actionOnId, String action, String msg, String logType) {
        AuditLogRecord auditLogRecord = AuditLogRecord.logBuilder()
                .withActionOnType(actionOnType)
                .withActionOnId(actionOnId)
                .withAction(action)
                .withmsg(msg)
                .withDateTime(new Timestamp(System.currentTimeMillis()))
                .withUsername(IMConstants.SYSTEM)
                .withLogType(logType)
                .build();

        AuditLog auditLog = new AuditLog();
        auditLog.setActionOnType(auditLogRecord.getActionOnType());
        auditLog.setActionOnId(auditLogRecord.getActionOnId());
        auditLog.setAction(auditLogRecord.getAction());
        auditLog.setUserName(auditLogRecord.getUsername());
        auditLog.setMsg(auditLogRecord.getMsg());
        auditLog.setDateTime(auditLogRecord.getDateTime());
        auditLogRepository.saveAndFlush(auditLog);
    }

    @Override
    public Long getTotalAuditLog() {
        return auditLogRepository.count();
    }

    @Override
    public AuditLog getAuditLogById(Long id) {
        return auditLogRepository.findOne(id);
    }

    @Override
    public List<AuditLog> getAuditLogRecords(int page, int size, Timestamp fromTime, Timestamp toTime, String action, String actionOnType, Long actionOnId, String logType) {
        return auditLogDetailRepository.getDetails(page, size, fromTime, toTime, action, actionOnType, actionOnId, logType);
    }

    @Override
    public Long getAuditLogRecordsCount(int page, int size, Timestamp fromTime, Timestamp toTime, String action, String actionOnType, Long actionOnId, String logType) {
        return auditLogDetailRepository.getCount(page, size, fromTime, toTime, action, actionOnType, actionOnId, logType);
    }
}
