package no.fjordkraft.afi.server.emuxml.services;

import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFile;
import no.fjordkraft.afi.server.emuxml.jpa.emu.Invoice;

public interface EmuXmlStoreService {

    /**
     * Stores EMU XML Invoice in the database.
     *
     * @param invoice
     */
    void store(EmuFile emuFile, Invoice invoice, String attachmentFileName);

}
