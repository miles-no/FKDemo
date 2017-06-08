package no.fjordkraft.im.repository;

import no.fjordkraft.im.domain.RestStatement;
import no.fjordkraft.im.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by miles on 6/7/2017.
 */
@Repository
public class StatementDetailRepository {

    @Autowired
    private EntityManager entityManager;

    public List<Statement> getDetails(int page, int size, String status, Timestamp fromTime,
                                Timestamp toTime){

        Query query = entityManager.createQuery("select s from Statement s where " +
                "(:status is null or s.status = :status) " +
                "and " +
                "(:fromTime is null or s.createTime >= :fromTime) " +
                "and " +
                "(:toTime is null or s.createTime <= :toTime)",Statement.class)
                .setFirstResult(page)
                .setMaxResults(size)
                .setParameter("status", status)
                .setParameter("fromTime", fromTime)
                .setParameter("toTime", toTime);

        List<Statement> statementList = query.getResultList();
        return statementList;
    }

    /*public List<Statement> getInvoicePdfs(Timestamp toTime){

        Query query = entityManager.createQuery("select s from Statement s where " +
                "(:status is null or s.status = :status) " +
                "and " +
                "(:fromTime is null or s.createTime >= :fromTime) " +
                "and " +
                "(:toTime is null or s.createTime <= :toTime)",Statement.class)
                .setFirstResult(page)
                .setMaxResults(size)
                .setParameter("status", status)
                .setParameter("fromTime", fromTime)
                .setParameter("toTime", toTime);

        List<Statement> statementList = query.getResultList();
        return statementList;
    }*/
}
