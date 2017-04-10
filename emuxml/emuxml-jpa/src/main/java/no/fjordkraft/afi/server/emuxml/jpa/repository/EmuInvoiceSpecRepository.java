package no.fjordkraft.afi.server.emuxml.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoice;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoiceOrderLine;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Slf4j
@Repository
public class EmuInvoiceSpecRepository {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void deleteEmuInvoice(Long invoiceNo) {
        entityManager.createQuery("" +
                "DELETE FROM " +
                "EmuInvoice " +
                "WHERE " +
                "invoiceNo = :invoiceNo ")
                .setParameter("invoiceNo", invoiceNo)
                .executeUpdate();
        entityManager.createQuery("" +
                "DELETE FROM " +
                "EmuInvoiceOrder " +
                "WHERE " +
                "invoiceNo = :invoiceNo ")
                .setParameter("invoiceNo", invoiceNo)
                .executeUpdate();
        entityManager.createQuery("" +
                "DELETE FROM " +
                "EmuInvoiceOrderLine " +
                "WHERE " +
                "invoiceNo = :invoiceNo ")
                .setParameter("invoiceNo", invoiceNo)
                .executeUpdate();
    }

    public Page<EmuInvoice> findByEmuInvoiceRequest(EmuInvoiceRequest request) {
        TypedQuery<EmuInvoice> q = entityManager.createQuery("" +
                        "SELECT ei " +
                        "FROM EmuInvoice ei " +
                        "WHERE " +
                        "ei.accountNo = :accountNo " +
                        "ORDER BY ei.invoiceNo " +
                        "",
                EmuInvoice.class)
                .setFirstResult(request.getPageRequest().getOffset())
                .setMaxResults(request.getPageRequest().getPageSize())
                .setParameter("accountNo", request.getAccountNo());

        List<EmuInvoice> list = q.getResultList();

        Query q2 = entityManager.createQuery("" +
                "SELECT COUNT(*) " +
                "FROM EmuInvoice ei " +
                "WHERE " +
                "ei.accountNo = :accountNo ")
                .setParameter("accountNo", request.getAccountNo());
        Object count = q2.getSingleResult();

        log.info(String.format("findByEmuInvoiceRequest: %s -> %d",
                request.toString(),
                ((Number) count).longValue()));

        return new PageImpl<EmuInvoice>(list, request.getPageRequest(), ((Number) count).longValue());
    }

    public List<EmuInvoiceOrderLine> findEmuInvoiceOrderLines(Long invoiceNo) {
        TypedQuery<EmuInvoiceOrderLine> q = entityManager.createQuery("" +
                        "SELECT eiol " +
                        "FROM EmuInvoiceOrderLine eiol " +
                        "WHERE " +
                        "eiol.invoiceNo = :invoiceNo " +
                        "ORDER BY eiol.eioid, eiol.eiolid " +
                        "",
                EmuInvoiceOrderLine.class)
                .setParameter("invoiceNo", invoiceNo);

        List<EmuInvoiceOrderLine> result = q.getResultList();
        return result;
    }

}
