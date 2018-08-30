package no.fjordkraft.im.services;

/**
 * Created by miles on 7/10/2017.
 */
public interface AuditLogService {

    void saveAuditLog(Long actionOnId, String action, String msg, String logType,String legalPartClass);
    void saveAuditLog(String actionOnType, Long actionOnId, String action, String msg, String logType,String legalPartClass);
}
