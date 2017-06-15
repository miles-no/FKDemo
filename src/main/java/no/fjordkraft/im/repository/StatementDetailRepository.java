package no.fjordkraft.im.repository;

import no.fjordkraft.im.domain.RestInvoicePdf;
import no.fjordkraft.im.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by miles on 6/7/2017.
 */
@Repository
public class StatementDetailRepository {

    @Autowired
    private EntityManager entityManager;

    final static String AND = " and ";
    final static String OR = " or ";

    @Transactional(readOnly = true)
    public List<Statement> getDetails(int page, int size, String status, Timestamp fromTime,
                                Timestamp toTime, String brand, String customerID, String invoiceNumber){

        StringBuffer selectQuery = new StringBuffer();
        selectQuery.append("select s from Statement s join s.systemBatchInput where ");
        if(null != status ) {
            selectQuery.append(addConditionForQueryValue(status, "s.status"));
            selectQuery.append(AND);
        }
        if(null != brand) {
            selectQuery.append(addConditionForQueryValue(brand, "s.systemBatchInput.brand"));
            selectQuery.append(AND);
        }
        selectQuery.append("(:invoiceNumber is null or s.invoiceNumber like :invoiceNumber) ");
        selectQuery.append(AND);
        selectQuery.append("(:customerID is null or s.customerId = :customerID) ");
        selectQuery.append(AND);
        selectQuery.append("(:fromTime is null or s.createTime >= :fromTime) ");
        selectQuery.append(AND);
        selectQuery.append("(:toTime is null or s.createTime <= :toTime) ");
        selectQuery.append("order by s.createTime desc");

        Query query = entityManager.createQuery(selectQuery.toString(), Statement.class)
                .setFirstResult(page)
                .setMaxResults(size)
                .setParameter("fromTime", fromTime)
                .setParameter("toTime", toTime)
                .setParameter("customerID", customerID)
                .setParameter("invoiceNumber", '%' + invoiceNumber + '%');

        List<Statement> statementList = query.getResultList();
        return statementList;
    }

    private String addConditionForQueryValue(String value, String name) {
        return "(" + name + " in " + "(" + value + "))";
    }

    public List<RestInvoicePdf> getInvoicePdfs(Collection<Long> ids) {
        //Query query = entityManager.createNativeQuery("Select ID,STATEMENT_ID statementId, type type from im_invoice_pdfs where statement_id in (:ids)").setParameter("ids",ids);
        Query query = entityManager.createQuery("Select id,statement.id, type from InvoicePdf ip where statement.id in (:ids)").setParameter("ids",ids);

        List<Object[]> rows = query.getResultList();
        List<RestInvoicePdf> restInvoicePdfList = new ArrayList<RestInvoicePdf>();
        for(Object[] row : rows){
            RestInvoicePdf invoicePdf = new RestInvoicePdf();
            invoicePdf.setId(Long.valueOf(row[0].toString()));
            invoicePdf.setStatementId(Long.valueOf(row[1].toString()));
            invoicePdf.setType(row[2].toString());
            restInvoicePdfList.add(invoicePdf);
        }
        return restInvoicePdfList;
    }

    @Transactional
    public Long getCountByStatus(String status) {
        String sql = "select count(s) from Statement s where s.status in (" + status + ")";
        Query query = entityManager.createQuery(sql);
        return (Long) query.getSingleResult();
    }
}
