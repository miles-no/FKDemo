package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.AuditLog;
import no.fjordkraft.im.model.Statement;
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
                                     String actionOnType, Long actionOnId, String logType, String invoiceNo) {

        StringBuffer selectQuery = new StringBuffer();
        selectQuery.append("select a from AuditLog a where ");

        selectQuery.append("(:action is null or a.action like :action) ");
        selectQuery.append(AND);
        selectQuery.append("(:actionOnType is null or a.actionOnType = :actionOnType) ");
        selectQuery.append(AND);
        selectQuery.append("(:actionOnId is null or a.actionOnId >= :actionOnId) ");
        selectQuery.append(AND);
        selectQuery.append("(:logType is null or a.logType >= :logType) ");
        selectQuery.append(AND);
        selectQuery.append("(:invoiceNo is null or a.invoiceNo >= :invoiceNo) ");
        selectQuery.append(AND);
        selectQuery.append("(:fromTime is null or a.dateTime >= :fromTime) ");
        selectQuery.append(AND);
        selectQuery.append("(:toTime is null or a.dateTime <= :toTime) ");
        selectQuery.append("order by a.dateTime desc");

        Query query = entityManager.createQuery(selectQuery.toString(), AuditLog.class)
                .setFirstResult(page*size)
                .setMaxResults(size)
                .setParameter("fromTime", fromTime)
                .setParameter("toTime", toTime)
                .setParameter("action", action)
                .setParameter("actionOnType", actionOnType)
                .setParameter("actionOnId", actionOnId)
                .setParameter("logType", logType)
                .setParameter("invoiceNo", invoiceNo);

        List<AuditLog> auditLogList = query.getResultList();
        return auditLogList;
    }

    @Transactional(readOnly = true)
    public Long getCount(int page, int size, Timestamp fromTime, Timestamp toTime, String action,
                                     String actionOnType, Long actionOnId, String logType, String invoiceNo) {

        StringBuffer selectQuery = new StringBuffer();
        selectQuery.append("select count(a) from AuditLog a where ");

        selectQuery.append("(:action is null or a.action like :action) ");
        selectQuery.append(AND);
        selectQuery.append("(:actionOnType is null or a.actionOnType = :actionOnType) ");
        selectQuery.append(AND);
        selectQuery.append("(:actionOnId is null or a.actionOnId >= :actionOnId) ");
        selectQuery.append(AND);
        selectQuery.append("(:logType is null or a.logType >= :logType) ");
        selectQuery.append(AND);
        selectQuery.append("(:invoiceNo is null or a.invoiceNo >= :invoiceNo) ");
        selectQuery.append(AND);
        selectQuery.append("(:fromTime is null or a.dateTime >= :fromTime) ");
        selectQuery.append(AND);
        selectQuery.append("(:toTime is null or a.dateTime <= :toTime) ");
        selectQuery.append("order by a.dateTime desc");

        Query query = entityManager.createQuery(selectQuery.toString(), Long.class)
                .setParameter("fromTime", fromTime)
                .setParameter("toTime", toTime)
                .setParameter("action", action)
                .setParameter("actionOnType", actionOnType)
                .setParameter("actionOnId", actionOnId)
                .setParameter("logType", logType)
                .setParameter("invoiceNo", invoiceNo);

        Long count = (Long) query.getSingleResult();
        return count;
    }
}
