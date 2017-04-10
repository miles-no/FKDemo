package no.fjordkraft.afi.server.emuxml.jpa.repository;

import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoiceOrder;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoiceOrderId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmuInvoiceOrderRepository extends JpaRepository<EmuInvoiceOrder, EmuInvoiceOrderId> {
}
