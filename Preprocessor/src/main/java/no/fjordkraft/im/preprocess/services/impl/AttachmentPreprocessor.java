package no.fjordkraft.im.preprocess.services.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.util.IMConstants;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxCategoryType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by miles on 9/8/2017.
 */
@Service
@PreprocessorInfo(order = 12)
public class AttachmentPreprocessor extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(AttachmentPreprocessor.class);

    @Autowired
    @Qualifier("unmarshaller")
    private Unmarshaller unMarshaller;

    @Autowired
    @Qualifier("marshaller")
    private Marshaller marshaller;

    private void generateAttachmentMap(Multimap<Long, Attachment> meterIdMapEMUXML, Map<String, Attachment> meterIdStartMonMapEMUXML, Attachments attachments, String invoicenumber) {
        for (int i = 0; i < attachments.getAttachment().size(); i++) {
            Attachment attachment = attachments.getAttachment().get(i);
            if (IMConstants.EMUXML.equals(attachment.getFAKTURA().getVEDLEGGFORMAT())) {
                logger.debug("Attachment with meterid " + attachment.getFAKTURA().getMAALEPUNKT() + " added to map ");
                meterIdMapEMUXML.put(attachment.getFAKTURA().getMAALEPUNKT(), attachment);
                if (null != attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getReadingInfo111()) {
                    XMLGregorianCalendar stromStartDate = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getReadingInfo111().getStartDate();
                    if (null != stromStartDate) {
                        meterIdStartMonMapEMUXML.put(attachment.getFAKTURA().getMAALEPUNKT() + "-" + stromStartDate.getMonth(), attachment);
                        logger.debug(" Meterid - month added to map " + attachment.getFAKTURA().getMAALEPUNKT() + "-" + stromStartDate.getMonth() + " invoice number " + invoicenumber);
                    }
                }
            }
        }
    }

    private void mapNettToStrom(Multimap<Long, Attachment> meterIdMapEMUXML, Map<String, Attachment> meterIdStartMonMapEMUXML, Attachments attachments, String invoicenumber) {
        for (int i = 0; i < attachments.getAttachment().size(); i++) {
            Attachment gridAttachment = attachments.getAttachment().get(i);
            if ((IMConstants.PDFEHF.equals(gridAttachment.getFAKTURA().getVEDLEGGFORMAT()) || IMConstants.PDFE2B.equals(gridAttachment.getFAKTURA().getVEDLEGGFORMAT()))
                    && meterIdMapEMUXML.containsKey(gridAttachment.getFAKTURA().getMAALEPUNKT())) {
                XMLGregorianCalendar gridStartDate = null;
                if (IMConstants.PDFEHF.equals(gridAttachment.getFAKTURA().getVEDLEGGFORMAT())) {
                    if (null != gridAttachment.getFAKTURA().getVedleggehfObj()) {
                        gridStartDate = gridAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getInvoiceLines().get(0).getInvoicePeriods().get(0).getStartDate().getValue();
                    }
                } else if (IMConstants.PDFE2B.equals(gridAttachment.getFAKTURA().getVEDLEGGFORMAT())) {
                    gridStartDate = gridAttachment.getFAKTURA().getVedlegge2BObj().getInvoice().getInvoiceDetails().getBaseItemDetails().get(0).getStartDate();
                }

                logger.debug(" MeterId and month " + gridAttachment.getFAKTURA().getMAALEPUNKT() + "-" + gridStartDate.getMonth() + " invoice number " + invoicenumber);
                Attachment stromAttachment = meterIdStartMonMapEMUXML.get(gridAttachment.getFAKTURA().getMAALEPUNKT() + "-" + gridStartDate.getMonth());
                boolean isMeterMonthStromAttachmentFound = false;
                if (null == stromAttachment) {
                    logger.debug(" Strom with same meterid and month not found" + " invoice number " + invoicenumber);
                    Collection attachCollection = meterIdMapEMUXML.get(gridAttachment.getFAKTURA().getMAALEPUNKT());
                    if (null != attachCollection && attachCollection.size() > 0) {
                        logger.debug(" Strom with same meterid found" + " invoice number " + invoicenumber);
                        stromAttachment = (Attachment) attachCollection.iterator().next();
                    }
                } else {
                    isMeterMonthStromAttachmentFound = true;
                }

                Nettleie nettleie = null;
                if (null != stromAttachment) {
                    if (IMConstants.PDFEHF.equals(gridAttachment.getFAKTURA().getVEDLEGGFORMAT())) {
                        logger.debug("createEHFEntry " + " invoice number " + invoicenumber);
                        nettleie = createEHFEntry(gridAttachment);
                    } else if (IMConstants.PDFE2B.equals(gridAttachment.getFAKTURA().getVEDLEGGFORMAT())) {
                        logger.debug("createE2BEntry " + " invoice number " + invoicenumber);
                        nettleie = createE2BEntry(gridAttachment);
                    }
                    List<Nettleie> nettleieList = stromAttachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleieList();
                    if (isMeterMonthStromAttachmentFound && nettleieList.size() == 0) {
                        nettleieList.add(nettleie);
                    } else if (isMeterMonthStromAttachmentFound && nettleieList.size() > 0) {
                        logger.debug("Moved nettleie to top of list");
                        Nettleie temp = nettleieList.get(0);
                        nettleieList.set(0, nettleie);
                        nettleieList.add(temp);
                    } else {
                        nettleieList.add(nettleie);
                    }
                }
            }
        }
    }

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {

        try {
            String invoicenumber = request.getEntity().getInvoiceNumber();
            Attachments attachments = request.getStatement().getAttachments();
            List<Attachment> attachmentList = new ArrayList<>();
            Multimap<Long, Attachment> meterIdMapEMUXML = ArrayListMultimap.create();
            Map<String, Attachment> meterIdStartMonMapEMUXML = new HashMap<>();

            generateAttachmentMap(meterIdMapEMUXML, meterIdStartMonMapEMUXML, attachments, invoicenumber);

            logger.debug("meterIdMapEMUXML size " + meterIdMapEMUXML.size() + " invoice number " + invoicenumber);
            logger.debug("meterIdStartMonMapEMUXML size " + meterIdStartMonMapEMUXML.size() + " invoice number " + invoicenumber);

            mapNettToStrom(meterIdMapEMUXML, meterIdStartMonMapEMUXML, attachments, invoicenumber);

            int index = 1;
            for (Attachment attachment : meterIdMapEMUXML.values()) {
                List<Nettleie> nettleieList = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleieList();
                logger.debug(" nettleieList "+ nettleieList.size());
                if (null != nettleieList && nettleieList.size() >= 1) {
                    attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setNettleie(nettleieList.get(0));
                    attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setNettleieList(null);
                    attachment.setDisplayStromData(true);
                    attachmentList.add(attachment);
                    for (int i = 1; i < nettleieList.size(); i++) {
                        Attachment stromAttachment = deepClone(attachment);
                        stromAttachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setNettleie(nettleieList.get(i));
                        stromAttachment.setDisplayStromData(false);
                        attachmentList.add(stromAttachment);
                    }
                } else {
                    attachmentList.add(attachment);
                }
            }

            for(Attachment attachment: attachmentList) {
                attachment.setAttachmentNumber(index++);
            }
            attachments.setAttachment(attachmentList);
            request.getStatement().setTotalAttachment(attachmentList.size());
        } catch (Exception e) {
            logger.debug("Exception in attachment preprocessor", e);
            throw new PreprocessorException(e);
        }
    }

    private Attachment deepClone(Attachment stromAttachment) {
        try {
            logger.debug("cloning stromAttachment " + stromAttachment.getFAKTURA().getMAALEPUNKT());

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            marshaller.marshal(stromAttachment, new StreamResult(byteArrayOutputStream));

            StreamSource source = new StreamSource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            Attachment attachment = (Attachment) unMarshaller.unmarshal(source);
            attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setNettleie(null);
            logger.debug("cloning stromAttachment " + stromAttachment.getFAKTURA().getMAALEPUNKT());
            attachment.setDisplayStromData(false);
            return attachment;
        } catch (Exception e) {
            throw new PreprocessorException(e);
        }
    }

    private Nettleie createEHFEntry(Attachment pdfAttachment) {
        logger.debug("createEHFEntry " + pdfAttachment.getFAKTURA().getMAALEPUNKT());
        Nettleie nettleie = new Nettleie();
        InvoiceSummary invoiceSummary = new InvoiceSummary();
        InvoiceTotals invoiceTotals = new InvoiceTotals();
        invoiceSummary.setInvoiceTotals(invoiceTotals);
        BaseItemDetails baseItemDetails;
        Float taxAmount = 0.0f;
        List<BaseItemDetails> baseItemDetailsList = new ArrayList<>();
        Invoice invoice = pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice();

        nettleie.setReferenceNumber(invoice.getID().getValue().toString());

        invoiceSummary.getInvoiceTotals().setGrossAmount(Float.valueOf(pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice()
                .getLegalMonetaryTotal().getTaxInclusiveAmount().getValue().toString()));

        invoiceSummary.getInvoiceTotals().setVatTotalsAmount(Float.valueOf(pdfAttachment.getFAKTURA().getVedleggehfObj()
                .getInvoice().getTaxTotals().get(0).getTaxSubtotals().get(0).getTaxAmount().getValue().toString()));

        nettleie.setInvoiceSummary(invoiceSummary);

        List<InvoiceLineType> invoiceLineTypeList = invoice.getInvoiceLines();
        for (InvoiceLineType invoiceLineType : invoiceLineTypeList) {
            baseItemDetails = new BaseItemDetails();
            baseItemDetails.setDescription(invoiceLineType.getItem().getName().getValue().toString());
            baseItemDetails.setQuantityInvoiced(Float.valueOf(invoiceLineType.getInvoicedQuantity().getValue().toString()));
            baseItemDetails.setUnitOfMeasure(invoiceLineType.getInvoicedQuantity().getUnitCode());
            baseItemDetails.setUnitPrice(Float.valueOf(invoiceLineType.getPrice().getPriceAmount().getValue().toString()));
            baseItemDetails.setPriceDenomination(invoiceLineType.getPrice().getPriceAmount().getCurrencyID());
            List<TaxCategoryType> categoryTypeList = invoiceLineType.getItem().getClassifiedTaxCategories();
            BigDecimal vat = null;
            for(TaxCategoryType taxCategoryType: categoryTypeList) {
                logger.debug("taxScheme " + taxCategoryType.getTaxScheme().getID().getValue()+ " vat "+ taxCategoryType.getPercent().getValue());
                if("VAT".equals(taxCategoryType.getTaxScheme().getID().getValue())) {
                    vat = taxCategoryType.getPercent().getValue();
                    break;
                }
            }
            if(null != vat) {
                baseItemDetails.setUnitPriceGross(baseItemDetails.getUnitPrice() + ((vat.floatValue()/100)*baseItemDetails.getUnitPrice()));
            } else {
                baseItemDetails.setUnitPriceGross(baseItemDetails.getUnitPrice());
            }

            if (null != invoiceLineType.getTaxTotals() && invoiceLineType.getTaxTotals().size() > 0 && null != invoiceLineType.getTaxTotals().get(0)) {
                taxAmount = invoiceLineType.getTaxTotals().get(0).getTaxAmount().getValue().floatValue();
                if (null != invoiceLineType.getInvoicePeriods()
                        && IMConstants.ZERO != invoiceLineType.getInvoicePeriods().size()) {
                    baseItemDetails.setStartDate(invoiceLineType.getInvoicePeriods().get(0).getStartDate().getValue());
                    baseItemDetails.setEndDate(invoiceLineType.getInvoicePeriods().get(0).getEndDate().getValue());
                }
            }
            if(null != baseItemDetails.getStartDate() && null != baseItemDetails.getEndDate()) {
                baseItemDetails.setNoOfDays(getDays(baseItemDetails.getStartDate(), baseItemDetails.getEndDate()));
            }
            baseItemDetails.setLineItemGrossAmount(Float.valueOf(invoiceLineType.getLineExtensionAmount().getValue().toString()) + taxAmount);
            baseItemDetailsList.add(baseItemDetails);
        }

        nettleie.setBaseItemDetails(baseItemDetailsList);
        return nettleie;
    }

    private Nettleie createE2BEntry(Attachment pdfAttachment) {
        logger.debug("createEHFEntry " + pdfAttachment.getFAKTURA().getMAALEPUNKT());
        Nettleie nettleie = new Nettleie();
        no.fjordkraft.im.if320.models.Invoice invoice = pdfAttachment.getFAKTURA().getVedlegge2BObj().getInvoice();

        nettleie.setReferenceNumber(String.valueOf(invoice.getInvoiceHeader().getInvoiceNumber()));
        List<BaseItemDetails> baseItemDetailsList = invoice.getInvoiceDetails().getBaseItemDetails();
        for (BaseItemDetails baseItemDetails : baseItemDetailsList) {
            List<Ref> refList = baseItemDetails.getRef();
            baseItemDetails.setUnitPriceGross(baseItemDetails.getUnitPrice());
            if (null != refList) {
                for (Ref ref : refList) {
                    if (IMConstants.CODE_UNIT_PRICE_GROSS.equals(ref.getCode())) {
                        baseItemDetails.setUnitPriceGross(Float.valueOf(ref.getText()));
                    }
                }
            }
            if(null != baseItemDetails.getStartDate() && null != baseItemDetails.getEndDate()) {
                baseItemDetails.setNoOfDays(getDays(baseItemDetails.getStartDate(),baseItemDetails.getEndDate()));
            }
        }


        nettleie.setBaseItemDetails(invoice.getInvoiceDetails().getBaseItemDetails());
        nettleie.setInvoiceSummary(invoice.getInvoiceSummary());
        return nettleie;
    }



    private long getDays(XMLGregorianCalendar startDate, XMLGregorianCalendar endDate){

        LocalDate localStartDate = LocalDate.of(startDate.getYear(),startDate.getMonth(),startDate.getDay());
        LocalDate localEndDate = LocalDate.of(endDate.getYear(),endDate.getMonth(),endDate.getDay());
        long days = ChronoUnit.DAYS.between(localStartDate,localEndDate);
        return days;
    }
}
