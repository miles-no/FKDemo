package no.fjordkraft.afi.server.emuxml.jpa.repository;

import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoiceOrderLine;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoiceOrderLineId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmuInvoiceOrderLineRepository extends JpaRepository<EmuInvoiceOrderLine, EmuInvoiceOrderLineId> {
}
