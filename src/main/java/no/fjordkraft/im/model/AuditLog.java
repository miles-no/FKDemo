package no.fjordkraft.im.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by miles on 7/7/2017.
 */
@Entity
@Table(name="IM_AUDIT_LOG")
public class AuditLog {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_AUDIT_LOG_SEQ")
    private Long id;

    @Column(name="ACTION_ON_TYPE")
    private String actionOnType;

    @Column(name="ACTION_ON_ID")
    private Long actionOnId;

    @Column(name="ACTION")
    private String action;

    @Column(name="USERNAME")
    private String userName;

    @Column(name="MSG")
    @Lob
    private String msg;

    @Column(name="DATETIME")
    private Timestamp dateTime;

    @Column(name="LOG_TYPE")
    private String logType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActionOnType() {
        return actionOnType;
    }

    public void setActionOnType(String actionOnType) {
        this.actionOnType = actionOnType;
    }

    public Long getActionOnId() {
        return actionOnId;
    }

    public void setActionOnId(Long actionOnId) {
        this.actionOnId = actionOnId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public AuditLog() {}

    AuditLog(String actionOnType, Long actionOnId, String action, String userName, String msg, Timestamp dateTime, String logType) {
        this.actionOnType = actionOnType;
        this.actionOnId = actionOnId;
        this.action = action;
        this.userName = userName;
        this.msg = msg;
        this.dateTime = dateTime;
        this.logType = logType;
    }

    @Override
    public String toString() {
        return "Audit_Log{ +" +
                "actionOnType=" + actionOnType +
                "actionOnId=" + actionOnId +
                "action=" + action +
                "userName=" + userName +
                "msg=" + msg +
                "dateTime=" + dateTime +
                "logType=" + logType +
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

        public AuditLog build() {
            return new AuditLog(
                    actionOnType,
                    actionOnId,
                    action,
                    username,
                    msg,
                    dateTime,
                    logType);
        }
    }
}
