package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.InvoicePdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by bhavi on 5/11/2017.
 */
public interface InvoicePdfRepository  extends JpaRepository<InvoicePdf,Long>  {

    @Query("select i from InvoicePdf i where i.statement.id = :statementId")
    List<InvoicePdf> getInvoicePDFsByStatementId(@Param("statementId") Long statementId);
}
