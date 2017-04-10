package no.fjordkraft.afi.server.emuxml.services.impl;

import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.basis.jpa.domain.ShortCustomerTypeEnum;
import no.fjordkraft.afi.server.basis.services.BrandConfigService;
import no.fjordkraft.afi.server.emuxml.jpa.emu.Invoice;
import no.fjordkraft.afi.server.emuxml.services.AttachmentStoreService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service("AttachmentStoreService")
public class AttachmentStoreServiceImpl implements AttachmentStoreService {

    @Override
    public String createAttachmentFileName(Invoice invoice) {
        return shouldStoreAttachment(invoice) ? "ISCU_" + invoice.getBrand() + "_" + invoice.getInvoiceNo() + ".xml" : null;
    }

    @Override
    public void storeAttachmentXml(Invoice invoice, String attachmentFileName, String toPath) {
        if (!shouldStoreAttachment(invoice)) {
            return;
        }

        toPath = toPath.replace(BrandConfigService.BRAND_CODE, invoice.getBrand() != null ? invoice.getBrand() : "");
        String toFile = toPath + File.separator + attachmentFileName;

        try {
            FileUtils.forceMkdir(new File(toPath));
            XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(new FileOutputStream(toFile), StandardCharsets.ISO_8859_1));
            writer.writeStartDocument("ISO-8859-1", "1.0");
            invoice.writeXml(writer);
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (XMLStreamException | IOException e) {
            throw new RuntimeException("Exception storing attachment XML", e);
        }
    }

    /**
     * No attachments for Private and invoiceType = K (Kreditfaktura)
     *
     * @param invoice
     * @return
     */
    private boolean shouldStoreAttachment(Invoice invoice) {
        return !(ShortCustomerTypeEnum.P.equals(invoice.getCustomerType()) && invoice.getAllCreditInvoice());
    }

}
