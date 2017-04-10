package no.fjordkraft.afi.server.emuxml.services.impl;

import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuXmlHandleRequest;
import no.fjordkraft.afi.server.emuxml.jpa.emu.BaseEmuParser;
import no.fjordkraft.afi.server.emuxml.jpa.emu.Invoice;
import no.fjordkraft.afi.server.emuxml.jpa.emu.InvoiceOrder;
import no.fjordkraft.afi.server.emuxml.services.IF318StoreService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service("IF318StoreService")
public class IF318StoreServiceImpl extends BaseEmuParser implements IF318StoreService {

    @Override
    public XMLStreamWriter createIF318Writer(EmuXmlHandleRequest request, String to318Path)
            throws IOException {
        if (!request.isStoreIF318File()) {
            return null;
        }
        FileUtils.forceMkdir(new File(to318Path));
        return createFile(to318Path + File.separator + request.getEmuXmlFilename(),
                request.getNumTransactions());
    }

    @Override
    public XMLStreamWriter createFile(String filename, long numTransactions) {
        XMLStreamWriter if318Writer = null;
        try {
            if318Writer = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8));

            if318Writer.writeStartDocument("utf-8", "1.0");
            if318Writer.writeStartElement("ChargeTransactionImport");
            if318Writer.writeStartElement("Header");
            write(if318Writer, "SourceSystem", "ISCU");
            write(if318Writer, "NumberOfRecords", "" + numTransactions);
            if318Writer.writeEndElement();
            if318Writer.writeStartElement("Transactions");

        } catch (XMLStreamException | FileNotFoundException e) {
            throw new RuntimeException(String.format("Unable to create IF318 file: %s", filename), e);
        }

        return if318Writer;
    }

    @Override
    public void store318Xml(Invoice invoice, XMLStreamWriter writer, String specificationFile) throws XMLStreamException {
        if (writer == null) {
            return;
        }
        writer.writeStartElement("Transaction");
        write(writer, "Brand", invoice.getBrand());
        write(writer, "AccountType", "");
        write(writer, "CustomerReference", "");
        write(writer, "AccountNumber", invoice.getAccountNo());
        write(writer, "TypeOfCharge", invoice.getTypeOfChange());
        write(writer, "Reference", "" + invoice.getInvoiceNo());
        write(writer, "TransactionDate", formatDate(invoice.getMainInvoiceInfo101().getCurrent().getPrintDate()));
        write(writer, "AmountExclusiveOfVat", fixAmount(invoice.getMainInvoiceInfo101().getCurrent().getNetPrintet()));
        write(writer, "VatTypeOfCharge", invoice.getVatTypeOfCharge());
        write(writer, "VatAmount", fixAmount(invoice.getMainInvoiceInfo101().getCurrent().getVAT()));
        write(writer, "Url", "");
        writeCData(writer, "FreeText", invoice.getFirstInvoiceOrder() != null ?
                invoice.getFirstInvoiceOrder().getShortAddress() : "");
        writer.writeStartElement("Distributions");
        for (InvoiceOrder invoiceOrder : invoice.getInvoiceOrder()) {
            if (invoiceOrder.shallAddDistribution()) {
                addDistribution(writer, invoiceOrder);
            }
        }
        writer.writeEndElement();
        write(writer, "SpecificationFile", specificationFile);
        writer.writeEndElement();
    }

    private void addDistribution(XMLStreamWriter writer, InvoiceOrder invoiceOrder)
            throws XMLStreamException {
        writer.writeStartElement("Distribution");
        write(writer, "Code", "KRAFT");
        write(writer, "Amount", fixAmount(invoiceOrder.getInvoiceOrderInfo110().getCurrent().getAmount()));
        writeCData(writer, "Company", invoiceOrder.getInvoiceOrderInfo110().getCurrent().getLDC1());
        writer.writeStartElement("DeliverySite");
        write(writer, "SiteId", invoiceOrder.getSupplyPointInfo117().getCurrent().getObjectId());
        write(writer, "GridArea", invoiceOrder.getSupplyPointInfo117().getCurrent().getGridArea());
        writeCData(writer, "Address", invoiceOrder.getAddress());
        write(writer, "Disconnected", invoiceOrder.getDisconnected());
        write(writer, "CustomerMoved", "0");
        writer.writeEndElement();
        writer.writeEndElement();
    }

    @Override
    public void closeFile(XMLStreamWriter writer) {
        if (writer == null) {
            return;
        }
        try {
            writer.writeEndElement();
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (XMLStreamException e) {
            log.error("Unable to close IF318 file", e);
        }
    }

}
