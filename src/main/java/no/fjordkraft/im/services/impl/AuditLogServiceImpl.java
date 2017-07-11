package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.AuditLog;
import no.fjordkraft.im.repository.AuditLogRepository;
import no.fjordkraft.im.services.AuditLogService;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Created by miles on 7/10/2017.
 */
@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    AuditLogRepository auditLogRepository;

    @Override
    public void saveAuditLog(Long actionOnId, String action, String msg, String logType) {
        AuditLog auditLog = AuditLog.logBuilder()
                .withActionOnType(IMConstants.STATEMENT)
                .withActionOnId(actionOnId)
                .withAction(action)
                .withmsg(msg)
                .withDateTime(new Timestamp(System.currentTimeMillis()))
                .withUsername(IMConstants.SYSTEM)
                .withLogType(logType)
                .build();

        auditLogRepository.saveAndFlush(auditLog);
    }

    @Override
    public void saveAuditLog(String actionOnType, Long actionOnId, String action, String msg, String logType) {
        AuditLog auditLog = AuditLog.logBuilder()
                .withActionOnType(actionOnType)
                .withActionOnId(actionOnId)
                .withAction(action)
                .withmsg(msg)
                .withDateTime(new Timestamp(System.currentTimeMillis()))
                .withUsername(IMConstants.SYSTEM)
                .withLogType(logType)
                .build();

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
}
