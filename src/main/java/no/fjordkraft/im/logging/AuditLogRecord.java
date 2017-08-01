package no.fjordkraft.im.logging;

import com.carfey.jdk.lang.DateTime;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by miles on 7/7/2017.
 */
public class AuditLogRecord {

    private String actionOnType;
    private Long actionOnId;
    private String action;
    private String username;
    private String msg;
    private Timestamp dateTime;
    private String logType;
    private String invoiceNo;

    AuditLogRecord(String actionOnType, Long actionOnId, String action, String username, String msg, Timestamp dateTime, String logType, String invoiceNo) {
        this.actionOnType = actionOnType;
        this.actionOnId = actionOnId;
        this.action = action;
        this.username = username;
        this.msg = msg;
        this.dateTime = dateTime;
        this.logType = logType;
        this.invoiceNo = invoiceNo;
    }

    public String getActionOnType() {
        return actionOnType;
    }

    public Long getActionOnId() {
        return actionOnId;
    }

    public String getAction() {
        return action;
    }

    public String getUsername() {
        return username;
    }

    public String getMsg() {
        return msg;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public String getLogType() {
        return logType;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    @Override
    public String toString() {
        return "Audit_Log{ +" +
                "actionOnType=" + actionOnType +
                "actionOnId=" + actionOnId +
                "action=" + action +
                "username=" + username +
                "msg=" + msg +
                "dateTime=" + dateTime +
                "logType=" + logType +
                "invoiceNo" + invoiceNo +
                "}";
    }

    public static AuditLogRecordBuilder logBuilder() {
        return new AuditLogRecordBuilder();
    }

    public static class AuditLogRecordBuilder {
        private String actionOnType;
        private Long actionOnId;
        private String action;
        private String username;
        private String msg;
        private Timestamp dateTime;
        private String logType;
        private String invoiceNo;

        public AuditLogRecordBuilder() {
            dateTime = new Timestamp(System.currentTimeMillis());
        }

        public AuditLogRecordBuilder withActionOnType(String actionOnType) {
            this.actionOnType = actionOnType;
            return this;
        }

        public AuditLogRecordBuilder withActionOnId(Long actionOnId) {
            this.actionOnId = actionOnId;
            return this;
        }

        public AuditLogRecordBuilder withAction(String action) {
            this.action = action;
            return this;
        }

        public AuditLogRecordBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public AuditLogRecordBuilder withmsg(String msg) {
            this.msg = msg;
            return this;
        }

        public AuditLogRecordBuilder withDateTime(Timestamp dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public AuditLogRecordBuilder withLogType(String logType) {
            this.logType = logType;
            return this;
        }

        public AuditLogRecordBuilder withInvoiceNo(String invoiceNo) {
            this.invoiceNo = invoiceNo;
            return this;
        }

        public AuditLogRecord build() {
            return new AuditLogRecord(
                    actionOnType,
                    actionOnId,
                    action,
                    username,
                    msg,
                    dateTime,
                    logType,
                    invoiceNo);
        }
    }
}
