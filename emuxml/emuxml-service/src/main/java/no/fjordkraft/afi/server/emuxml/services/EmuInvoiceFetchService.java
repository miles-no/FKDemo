package no.fjordkraft.afi.server.emuxml.services;

import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoice;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoiceRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmuInvoiceFetchService {

    /**
     * Fetch EMU invoices by request.
     *
     * @param request
     * @return
     */
    Page<EmuInvoice> filterByRequest(EmuInvoiceRequest request);

}
