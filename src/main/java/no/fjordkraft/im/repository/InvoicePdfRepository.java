package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.InvoicePdf;
import no.fjordkraft.im.model.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bhavi on 5/11/2017.
 */
public interface InvoicePdfRepository  extends JpaRepository<InvoicePdf,Long>  {
}
