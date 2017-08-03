package no.fjordkraft.im.domain;

import java.sql.Timestamp;

/**
 * Created by miles on 8/3/2017.
 */
public class RestAuditLog {

    private String actionOnType;
    private Long actionOnId;
    private String action;
    private String userName;
    private String msg;
    private Timestamp dateTime;
    private String logType;
    private String invoiceNo;
    private String accountNumber;
    private String customerId;

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

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
