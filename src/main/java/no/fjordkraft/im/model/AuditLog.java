package no.fjordkraft.im.model;

import no.fjordkraft.im.logging.AuditLogRecord;

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
}
