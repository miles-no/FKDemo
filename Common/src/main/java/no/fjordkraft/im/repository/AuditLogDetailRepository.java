package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.AuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AuditLogDetailRepository.class);
    @Autowired
    private EntityManager entityManager;

    final static String AND = " and ";

    @Transactional(readOnly = true)
    public List<AuditLog> getDetails(int page, int size, Timestamp fromTime, Timestamp toTime, String action,
                                     String actionOnType, String logType, String invoiceNo, String customerID, String accountNumber,String legalPartClass) {

        StringBuffer selectQuery = new StringBuffer();
        selectQuery.append("select a from AuditLog a join a.statement where ");

        if(null != action) {
            selectQuery.append((addConditionForQueryValue(action, "a.action")));
            selectQuery.append(AND);
        }
        if(null != actionOnType) {
            selectQuery.append((addConditionForQueryValue(actionOnType, "a.actionOnType")));
            selectQuery.append(AND);
        }
        if(null != logType) {
            selectQuery.append((addConditionForQueryValue(logType, "a.logType")));
            selectQuery.append(AND);
        }
        if(null != invoiceNo) {
            selectQuery.append((addConditionForQueryValue(invoiceNo, "a.statement.invoiceNumber")));
            selectQuery.append(AND);
        }
        if(null != customerID) {
            selectQuery.append((addConditionForQueryValue(customerID, "a.statement.customerId")));
            selectQuery.append(AND);
        }
        if(null != accountNumber) {
            selectQuery.append((addConditionForQueryValue(accountNumber, "a.statement.accountNumber")));
            selectQuery.append(AND);
        }

        if(null != legalPartClass) {
            selectQuery.append((addConditionForQueryValue(accountNumber, "a.statement.legalPartClass")));
            selectQuery.append(AND);
        }
        selectQuery.append("(:fromTime is null or a.dateTime >= :fromTime) ");
        selectQuery.append(AND);
        selectQuery.append("(:toTime is null or a.dateTime <= :toTime) ");
        selectQuery.append("order by a.dateTime desc");
         logger.debug("Query : " + selectQuery.toString());
        Query query = entityManager.createQuery(selectQuery.toString(), AuditLog.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .setParameter("fromTime", fromTime)
                .setParameter("toTime", toTime);

        List<AuditLog> auditLogList = query.getResultList();
        return auditLogList;
    }

    @Transactional(readOnly = true)
    public Long getCount(Timestamp fromTime, Timestamp toTime, String action,
                                     String actionOnType, String logType, String invoiceNo, String customerID, String accountNumber,String legalPartClass) {

        StringBuffer selectQuery = new StringBuffer();
        selectQuery.append("select count(a) from AuditLog a join a.statement where ");

        if(null != action) {
            selectQuery.append((addConditionForQueryValue(action, "a.action")));
            selectQuery.append(AND);
        }
        if(null != actionOnType) {
            selectQuery.append((addConditionForQueryValue(actionOnType, "a.actionOnType")));
            selectQuery.append(AND);
        }
        if(null != logType) {
            selectQuery.append((addConditionForQueryValue(logType, "a.logType")));
            selectQuery.append(AND);
        }
        if(null != invoiceNo) {
            selectQuery.append((addConditionForQueryValue(invoiceNo, "a.statement.invoiceNumber")));
            selectQuery.append(AND);
        }
        if(null != customerID) {
            selectQuery.append((addConditionForQueryValue(customerID, "a.statement.customerId")));
            selectQuery.append(AND);
        }
        if(null != accountNumber) {
            selectQuery.append((addConditionForQueryValue(accountNumber, "a.statement.accountNumber")));
            selectQuery.append(AND);
        }

        if(null != legalPartClass) {
            selectQuery.append((addConditionForQueryValue(accountNumber, "a.statement.legalPartClass")));
            selectQuery.append(AND);
        }
        selectQuery.append("(:fromTime is null or a.dateTime >= :fromTime) ");
        selectQuery.append(AND);
        selectQuery.append("(:toTime is null or a.dateTime <= :toTime) ");
        selectQuery.append("order by a.dateTime desc");

        Query query = entityManager.createQuery(selectQuery.toString(), Long.class)
                .setParameter("fromTime", fromTime)
                .setParameter("toTime", toTime);

        Long count = (Long) query.getSingleResult();
        return count;
    }

    private String addConditionForQueryValue(String value, String name) {
        return "(" + name + " in " + "('" + value + "'))";
    }
}
