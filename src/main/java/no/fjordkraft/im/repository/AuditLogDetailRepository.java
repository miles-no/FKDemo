package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.AuditLog;
import no.fjordkraft.im.model.Statement;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by miles on 7/24/2017.
 */
@Repository
public class AuditLogDetailRepository {

    @Autowired
    private EntityManager entityManager;

    final static String AND = " and ";

    @Transactional(readOnly = true)
    public List<AuditLog> getDetails(int page, int size, Timestamp fromTime, Timestamp toTime, String action,
                                     String actionOnType, String logType, String invoiceNo, String customerID, String accountNumber) {

        StringBuffer selectQuery = new StringBuffer();
        selectQuery.append("select a from AuditLog a join a.statement where ");

        if(null != action) {
            selectQuery.append("(:action is null or a.action like :action) ");
            selectQuery.append(AND);
        }
        if(null != actionOnType) {
            selectQuery.append("(:actionOnType is null or a.actionOnType = :actionOnType) ");
            selectQuery.append(AND);
        }
        if(null != logType) {
            selectQuery.append("(:logType is null or a.logType = :logType) ");
            selectQuery.append(AND);
        }
        if(null != invoiceNo) {
            selectQuery.append("(:invoiceNo is null or a.statement.invoiceNumber = :invoiceNo) ");
            selectQuery.append(AND);
        }
        if(null != customerID) {
            selectQuery.append("(:customerID is null or a.statement.customerId = :customerID) ");
            selectQuery.append(AND);
        }
        if(null != accountNumber) {
            selectQuery.append("(:accountNumber is null or a.statement.accountNumber = :accountNumber) ");
            selectQuery.append(AND);
        }
        selectQuery.append("(:fromTime is null or a.dateTime >= :fromTime) ");
        selectQuery.append(AND);
        selectQuery.append("(:toTime is null or a.dateTime <= :toTime) ");
        selectQuery.append("order by a.dateTime desc");

        Query query = entityManager.createQuery(selectQuery.toString(), AuditLog.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .setParameter("fromTime", fromTime)
                .setParameter("toTime", toTime)
                .setParameter("action", action)
                .setParameter("actionOnType", actionOnType)
                .setParameter("logType", logType)
                .setParameter("invoiceNo", invoiceNo)
                .setParameter("customerID", customerID)
                .setParameter("accountNumber", accountNumber);

        List<AuditLog> auditLogList = query.getResultList();
        return auditLogList;
    }

    @Transactional(readOnly = true)
    public Long getCount(Timestamp fromTime, Timestamp toTime, String action,
                                     String actionOnType, String logType, String invoiceNo, String customerID, String accountNumber) {

        StringBuffer selectQuery = new StringBuffer();
        selectQuery.append("select count(a) from AuditLog a join a.statement where ");

        if(null != action) {
            selectQuery.append("(:action is null or a.action like :action) ");
            selectQuery.append(AND);
        }
        if(null != actionOnType) {
            selectQuery.append("(:actionOnType is null or a.actionOnType = :actionOnType) ");
            selectQuery.append(AND);
        }
        if(null != logType) {
            selectQuery.append("(:logType is null or a.logType = :logType) ");
            selectQuery.append(AND);
        }
        if(null != invoiceNo) {
            selectQuery.append("(:invoiceNo is null or a.statement.invoiceNumber = :invoiceNo) ");
            selectQuery.append(AND);
        }
        if(null != customerID) {
            selectQuery.append("(:customerID is null or a.statement.customerId = :customerID) ");
            selectQuery.append(AND);
        }
        if(null != accountNumber) {
            selectQuery.append("(:accountNumber is null or a.statement.accountNumber = :accountNumber) ");
            selectQuery.append(AND);
        }
        selectQuery.append("(:fromTime is null or a.dateTime >= :fromTime) ");
        selectQuery.append(AND);
        selectQuery.append("(:toTime is null or a.dateTime <= :toTime) ");
        selectQuery.append("order by a.dateTime desc");

        Query query = entityManager.createQuery(selectQuery.toString(), Long.class)
                .setParameter("fromTime", fromTime)
                .setParameter("toTime", toTime)
                .setParameter("action", action)
                .setParameter("actionOnType", actionOnType)
                .setParameter("logType", logType)
                .setParameter("invoiceNo", invoiceNo)
                .setParameter("customerID", customerID)
                .setParameter("accountNumber", accountNumber);

        Long count = (Long) query.getSingleResult();
        return count;
    }
}
