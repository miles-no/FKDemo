package no.fjordkraft.afi.server.emuxml.jpa.repository;

import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmuInvoiceRepository extends JpaRepository<EmuInvoice, Long> {
}
