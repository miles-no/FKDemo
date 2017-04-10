package no.fjordkraft.afi.server.emuxml.services;

import no.fjordkraft.afi.server.emuxml.jpa.emu.Invoice;

public interface AttachmentStoreService {

    /**
     * Creates attachment filename.
     *
     * Returns NULL if the attachment should not be stored.
     *
     * @param invoice
     * @return return attachment filename
     */
    String createAttachmentFileName(Invoice invoice);

    /**
     * Stores invoice as attachment file.
     *
     * @param invoice
     * @param attachmentFileName
     * @param toPath
     */
    void storeAttachmentXml(Invoice invoice, String attachmentFileName, String toPath);

}
