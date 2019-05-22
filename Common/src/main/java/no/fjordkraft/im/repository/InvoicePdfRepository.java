package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.InvoicePdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by bhavi on 5/11/2017.
 */
public interface InvoicePdfRepository  extends JpaRepository<InvoicePdf,Long>  {

    @Query("select i from InvoicePdf i where i.statement.id = :statementId")
    List<InvoicePdf> getInvoicePDFsByStatementId(@Param("statementId") Long statementId);

    @Transactional
    @Modifying
    @Query("delete from InvoicePdf i where i.statement.id in (select s.id from Statement s where  s.createTime <= :tillDate) ")
    int deleteInvoicePDFsTillDate(@Param("tillDate") Date tillDate);
}
