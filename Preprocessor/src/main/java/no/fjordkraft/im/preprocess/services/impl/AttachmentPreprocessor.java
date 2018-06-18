package no.fjordkraft.im.preprocess.services.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.AuditLogService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CreditNoteLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxCategoryType;
import oasis.names.specification.ubl.schema.xsd.creditnote_2.CreditNote;
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
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
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

    @Autowired
    AuditLogService auditLogService;

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
                        String monthName =  Month.of(stromStartDate.getMonth()).getDisplayName (TextStyle.FULL, new Locale("no","NO"));
                        int year =   stromStartDate.getYear();
                        attachment.setStartMonthYear(monthName+ " " + year );
                    }
                }
            }
        }
    }

    private void mapNettToStrom(Multimap<Long, Attachment> meterIdMapEMUXML, Map<String, Attachment> meterIdStartMonMapEMUXML, Attachments attachments, String invoicenumber,Long statementId) {
        for (int i = 0; i < attachments.getAttachment().size(); i++) {
            Attachment gridAttachment = attachments.getAttachment().get(i);
            if ((IMConstants.PDFEHF.equals(gridAttachment.getFAKTURA().getVEDLEGGFORMAT()) || IMConstants.PDFE2B.equals(gridAttachment.getFAKTURA().getVEDLEGGFORMAT()))){
                     if(meterIdMapEMUXML.containsKey(gridAttachment.getFAKTURA().getMAALEPUNKT())) {
                    XMLGregorianCalendar gridStartDate = null;
                    if (IMConstants.PDFEHF.equals(gridAttachment.getFAKTURA().getVEDLEGGFORMAT())) {
                        if (null != gridAttachment.getFAKTURA().getVedleggehfObj()) {
                            if("creditnote".equalsIgnoreCase(gridAttachment.getFAKTURA().getFAKTURATYPE().toLowerCase()))
                            {
                                    if(gridAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getCreditNoteLines()!=null && !gridAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getCreditNoteLines().isEmpty()
                                            && gridAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getCreditNoteLines().get(0)!=null && gridAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getCreditNoteLines().get(0).getInvoicePeriods()!=null && !gridAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getCreditNoteLines().get(0).getInvoicePeriods().isEmpty()
                                            && gridAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getCreditNoteLines().get(0).getInvoicePeriods().get(0).getStartDate()!=null) {
                                  gridStartDate = gridAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getCreditNoteLines().get(0).getInvoicePeriods().get(0).getStartDate().getValue();
                                    }
                            }
                            else if(gridAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getInvoiceLines()!=null && !gridAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getInvoiceLines().isEmpty()
                                        && gridAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getInvoiceLines().get(0)!=null && gridAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getInvoiceLines().get(0).getInvoicePeriods()!=null &&
                                    !gridAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getInvoiceLines().get(0).getInvoicePeriods().isEmpty() && gridAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getInvoiceLines().get(0).getInvoicePeriods().get(0).getStartDate()!=null)
                            {
                                    gridStartDate = gridAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getInvoiceLines().get(0).getInvoicePeriods().get(0).getStartDate().getValue();
                            }

                        }
                    } else if (IMConstants.PDFE2B.equals(gridAttachment.getFAKTURA().getVEDLEGGFORMAT())) {
                        gridStartDate = gridAttachment.getFAKTURA().getVedlegge2BObj().getInvoice().getInvoiceDetails().getBaseItemDetails().get(0).getStartDate();
                    }

               // logger.debug(" MeterId and month " + gridAttachment.getFAKTURA().getMAALEPUNKT() + "-" + gridStartDate.getMonth() + " invoice number " + invoicenumber);
                Attachment stromAttachment = null;
                if(gridStartDate!=null)  {
                             stromAttachment =  meterIdStartMonMapEMUXML.get(gridAttachment.getFAKTURA().getMAALEPUNKT() + "-" + gridStartDate.getMonth());
                }
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
                else  {
                         Attachment dummyStromAttachment = deepClone(gridAttachment);
                         String errorMessage = "Only Grid for Invoice Number";
                         auditLogService.saveAuditLog(statementId, StatementStatusEnum.PRE_PROCESSING.getStatus(), errorMessage, IMConstants.INFO);
                         if(IMConstants.PDFEHF.equals(gridAttachment.getFAKTURA().getVEDLEGGFORMAT()))
                         {
                             Nettleie nettleie = createEHFEntry(gridAttachment);
                             dummyStromAttachment.getFAKTURA().setFreeText(nettleie.getFreeText());
                             dummyStromAttachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getProductParameters118().setDescription(nettleie.getDescription());
                             dummyStromAttachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getSupplyPointInfo117().setObjectId(nettleie.getObjectId());
                             dummyStromAttachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getSupplyPointInfo117().setMeterId(nettleie.getMeterId());
                             dummyStromAttachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getYearlyConsumption123().setAnnualConsumption(nettleie.getAnnualConsumption());

                             dummyStromAttachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setNettleie(nettleie);
                             dummyStromAttachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setMapOfVatSumOfGross(new HashMap());
                             dummyStromAttachment.getFAKTURA().setFAKTURANR(null);
                             dummyStromAttachment.setDisplayStromData(false);
                             dummyStromAttachment.setOnlyGrid(true);
                             meterIdMapEMUXML.put(dummyStromAttachment.getFAKTURA().getMAALEPUNKT(),dummyStromAttachment);
                             logger.debug("No Strom attachment found then create a strom attachement and add nettleie to it ");
                             logger.debug("Only Grid Case");
                         }
                         if(IMConstants.PDFE2B.equals(gridAttachment.getFAKTURA().getVEDLEGGFORMAT()))
                         {
                             Nettleie nettleie = createE2BEntry(gridAttachment);

                             dummyStromAttachment.getFAKTURA().setFreeText(nettleie.getFreeText());
                             dummyStromAttachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getProductParameters118().setDescription(nettleie.getDescription());
                             dummyStromAttachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getSupplyPointInfo117().setObjectId(nettleie.getObjectId());
                             dummyStromAttachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getSupplyPointInfo117().setMeterId(nettleie.getMeterId());
                             dummyStromAttachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getYearlyConsumption123().setAnnualConsumption(nettleie.getAnnualConsumption());

                             dummyStromAttachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setNettleie(nettleie);
                             dummyStromAttachment.getFAKTURA().setFAKTURANR(null);
                             dummyStromAttachment.setDisplayStromData(false);
                             dummyStromAttachment.setOnlyGrid(true);
                             meterIdMapEMUXML.put(dummyStromAttachment.getFAKTURA().getMAALEPUNKT(),dummyStromAttachment);
                             logger.debug("No Strom attachment found then create a strom attachement and add nettleie to it ");
                             logger.debug("Only Grid Case");
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
          //  Multimap<Long, Attachment> meterIdVsOnlyGrid =ArrayListMultimap.create();

            generateAttachmentMap(meterIdMapEMUXML, meterIdStartMonMapEMUXML, attachments, invoicenumber);

            logger.debug("meterIdMapEMUXML size " + meterIdMapEMUXML.size() + " invoice number " + invoicenumber);
            logger.debug("meterIdStartMonMapEMUXML size " + meterIdStartMonMapEMUXML.size() + " invoice number " + invoicenumber);
            if(meterIdMapEMUXML.size()==1) {
                request.getStatement().setOneMeter(true);
            }

            mapNettToStrom(meterIdMapEMUXML, meterIdStartMonMapEMUXML, attachments, invoicenumber,request.getEntity().getId());

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
                        stromAttachment.getFAKTURA().setFAKTURANR(null);
                        stromAttachment.setDisplayStromData(false);
                        stromAttachment.setOnlyGrid(true);
                        attachmentList.add(stromAttachment);
                    }
                } else {
                    attachmentList.add(attachment);
                }

                boolean isSumMatching = true;
                for(Nettleie nettleie:nettleieList){
                    if(!nettleie.isSumMatching())
                    {
                        isSumMatching = false;
                        break;
                    }
                }
              /*  if(!isSumMatching)
                {
                    String errorMessage = "Sum of the grid lines is not matching with Sum Nettleie.";
                    auditLogService.saveAuditLog(new Long(request.getEntity().getInvoiceNumber()),StatementStatusEnum.PRE_PROCESSING.getStatus(), errorMessage, IMConstants.ERROR);
                    logger.debug("Exception in attachment preprocessor due to sum of grid lines is not matching with sum of nettelie");
                    throw  new PreprocessorException("Sum is not matching for invoice number " +request.getEntity().getInvoiceNumber()) ;
                }*/
            }

            for(Attachment attachment: attachmentList) {
                float sumStrom = 0.0f;
                if(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder()!=null && attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getInvoiceOrderAmounts113()!=null)
                {
                    if(request.getStatement().getLegalPartClass().equals("Individual")) {
                    sumStrom = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getInvoiceOrderAmounts113().getGrossTotal();
                    }
                    else {
                        sumStrom = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getMainInvoiceInfo101().getNetPrintet();
                    }
                }

                float sumNett = 0.0f;
                if( attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie()!=null) {
                    if(request.getStatement().getLegalPartClass().equals("Individual")) {
                    sumNett = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getInvoiceSummary().getInvoiceTotals().getOrigGrossAmount();
                    }
                    else {
                        sumNett = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie().getSumOfNettAmount();
                    }
                }
                attachment.setSumOfOutGoingCurrentClaim(request.getStatement().getOutgoingBalance()+request.getStatement().getCurrentClaim());
                attachment.setSumOfNettStrom(sumStrom+sumNett);
                attachment.setSumOfIngoingIBtrans(request.getStatement().getIngoingBalance()+request.getStatement().getTransactions().getIbAmountWithVat());
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
            if( attachment.getFAKTURA().getVEDLEGGEMUXML()!=null)
            {
                attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setNettleie(null);

            }
            else
            {
                attachment.getFAKTURA().setVEDLEGGEMUXML(new VEDLEGGEMUXML());
                attachment.getFAKTURA().getVEDLEGGEMUXML().setInvoice(new no.fjordkraft.im.if320.models.Invoice());
                List<InvoiceOrder> listofinvoiceOrder = new ArrayList();
                InvoiceOrder order = new InvoiceOrder();
                listofinvoiceOrder.add(order);
                attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().setInvoiceOrder(listofinvoiceOrder);
                attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setProductParameters118(new ProductParameters118());
                attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setSupplyPointInfo117(new SupplyPointInfo117());
                attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setReadingInfo111(new ReadingInfo111());
                attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setYearlyConsumption123(new YearlyConsumption123());
            }
           // attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setNettleie(null);
            logger.debug("cloning stromAttachment " + stromAttachment.getFAKTURA().getMAALEPUNKT());
            attachment.getFAKTURA().setFAKTURANR(null);
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
        if(pdfAttachment.getFAKTURA().getVedleggehfObj()!=null)
        {
            nettleie.setFakturanr(pdfAttachment.getFAKTURA().getFAKTURANR());
            if("creditnote".equalsIgnoreCase(pdfAttachment.getFAKTURA().getFAKTURATYPE().toLowerCase()))
            {
            CreditNote creditNote = pdfAttachment.getFAKTURA().getVedleggehfObj().getCreditNote();
            nettleie.setReferenceNumber(creditNote.getID().getValue().toString());
                if(creditNote.getDeliveries().get(0)!=null && creditNote.getDeliveries().get(0).getDeliveryLocation()!=null &&
                        creditNote.getDeliveries().get(0).getDeliveryLocation().getAddress()!=null
                        && creditNote.getDeliveries().get(0).getDeliveryLocation().getAddress().getStreetName()!=null)
                         nettleie.setFreeText(creditNote.getDeliveries().get(0).getDeliveryLocation().getAddress().getStreetName().getValue());
            nettleie.setDescription(null);
            nettleie.setObjectId((Long.valueOf(creditNote.getDeliveries().get(0).getDeliveryLocation().getID().getValue().toString())));
            nettleie.setMeterId(creditNote.getAccountingSupplierParty().getParty().getPartyLegalEntities().get(0).getCompanyID().getValue());
            nettleie.setAnnualConsumption(0);
            nettleie.setGridName(pdfAttachment.getFAKTURA().getAKTORNAVN());
            invoiceSummary.getInvoiceTotals().setGrossAmount(Float.valueOf(pdfAttachment.getFAKTURA().getVedleggehfObj().getCreditNote()
                    .getLegalMonetaryTotal().getTaxInclusiveAmount().getValue().toString())* IMConstants.NEGATIVE);

            invoiceSummary.getInvoiceTotals().setOrigGrossAmount(Float.valueOf(pdfAttachment.getFAKTURA().getVedleggehfObj().getCreditNote()
                        .getLegalMonetaryTotal().getTaxInclusiveAmount().getValue().toString()));

            if(pdfAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getTaxTotals()!=null && !pdfAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getTaxTotals().isEmpty() && pdfAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getTaxTotals().get(0).getTaxSubtotals()!=null && !pdfAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getTaxTotals().get(0).getTaxSubtotals().isEmpty()
                    && pdfAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getTaxTotals().get(0).getTaxSubtotals().get(0).getTaxAmount()!=null &&  pdfAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getTaxTotals().get(0).getTaxSubtotals().get(0).getTaxAmount().getValue()!=null) {
                invoiceSummary.getInvoiceTotals().setVatTotalsAmount(Float.valueOf(pdfAttachment.getFAKTURA().getVedleggehfObj()
                        .getCreditNote().getTaxTotals().get(0).getTaxSubtotals().get(0).getTaxAmount().getValue().toString())* IMConstants.NEGATIVE);
            } else if(pdfAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getTaxTotals()!=null && !pdfAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getTaxTotals().isEmpty()
                    && pdfAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getTaxTotals()!=null &&
                    pdfAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getTaxTotals().get(0).getTaxAmount()!=null && pdfAttachment.getFAKTURA().getVedleggehfObj().getCreditNote().getTaxTotals().get(0).getTaxAmount().getValue()!=null) {
                invoiceSummary.getInvoiceTotals().setVatTotalsAmount(Float.valueOf(pdfAttachment.getFAKTURA().getVedleggehfObj()
                        .getCreditNote().getTaxTotals().get(0).getTaxAmount().getValue().toString())* IMConstants.NEGATIVE);
            }

            nettleie.setInvoiceSummary(invoiceSummary);

            List<CreditNoteLineType> creditNoteLineTypeList = creditNote.getCreditNoteLines();
            Map<Float,Float> mapOfVatSumOfGross = new HashMap<Float,Float>();
            Float sumGrossAmount = 0.0f;
            float sumOfVatAmount = 0.0f;
            XMLGregorianCalendar startDate = null;
            for(CreditNoteLineType creditNoteLineType : creditNoteLineTypeList)
            {
                baseItemDetails = new BaseItemDetails();
                baseItemDetails.setDescription(creditNoteLineType.getItem().getName().getValue().toString());
                baseItemDetails.setUnitOfMeasure(creditNoteLineType.getCreditedQuantity().getUnitCode());
                if("kwh".equalsIgnoreCase(baseItemDetails.getUnitOfMeasure().toLowerCase()))  {
                    baseItemDetails.setQuantityInvoiced(Float.valueOf(creditNoteLineType.getCreditedQuantity().getValue().toString()) * IMConstants.NEGATIVE);
                }else
                {
                    baseItemDetails.setQuantityInvoiced(Float.valueOf(creditNoteLineType.getCreditedQuantity().getValue().toString()));
                }
                baseItemDetails.setUnitPrice(Float.valueOf(creditNoteLineType.getPrice().getPriceAmount().getValue().toString())* IMConstants.NEGATIVE);
                baseItemDetails.setPriceDenomination(creditNoteLineType.getPrice().getPriceAmount().getCurrencyID());
                baseItemDetails.setAttachmentFormat(pdfAttachment.getFAKTURA().getVEDLEGGFORMAT());
                List<TaxCategoryType> categoryTypeList = creditNoteLineType.getItem().getClassifiedTaxCategories();
                BigDecimal vat = null;
                for(TaxCategoryType taxCategoryType: categoryTypeList) {
                    logger.debug("taxScheme " + taxCategoryType.getTaxScheme().getID().getValue()+ " vat "+ taxCategoryType.getPercent().getValue());
                    if("VAT".equals(taxCategoryType.getTaxScheme().getID().getValue())) {
                        vat = taxCategoryType.getPercent().getValue();
                        break;
                    }
                }

                baseItemDetails.setVatRate(Float.valueOf(vat.toString()));
                baseItemDetails.setLineExtensionAmount(Float.valueOf(creditNoteLineType.getLineExtensionAmount().getValue().toString())*IMConstants.NEGATIVE);
                if(baseItemDetails.getVatInfo()==null) {
                    VatInfo vatInfo = new VatInfo();
                    if(creditNoteLineType.getTaxTotals()!=null && creditNoteLineType.getTaxTotals().size()>0) {
                    vatInfo.setVatAmount(Float.valueOf(creditNoteLineType.getTaxTotals().get(0).getTaxAmount().getValue().toString()));
                    }
                    baseItemDetails.setVatInfo(vatInfo);
                }

                if(mapOfVatSumOfGross.containsKey(baseItemDetails.getVatRate()))
                {
                    float vatAmount = mapOfVatSumOfGross.get(baseItemDetails.getVatRate());
                    vatAmount += baseItemDetails.getLineExtensionAmount(); //baseItemDetails.getVatInfo().getVatAmount();
                    mapOfVatSumOfGross.put(baseItemDetails.getVatRate(),vatAmount);
                } else {

                    mapOfVatSumOfGross.put(baseItemDetails.getVatRate(),baseItemDetails.getLineExtensionAmount());
                }
                if(null != vat) {
                    baseItemDetails.setUnitPriceGross((baseItemDetails.getUnitPrice() + ((vat.floatValue()/100)*baseItemDetails.getUnitPrice()))* IMConstants.NEGATIVE);
                } else {
                    baseItemDetails.setUnitPriceGross(baseItemDetails.getUnitPrice() * IMConstants.NEGATIVE);
                }

                if (null != creditNoteLineType.getTaxTotals() && creditNoteLineType.getTaxTotals().size() > 0 && null != creditNoteLineType.getTaxTotals().get(0)) {
                    taxAmount = creditNoteLineType.getTaxTotals().get(0).getTaxAmount().getValue().floatValue();
                }
                    if (null != creditNoteLineType.getInvoicePeriods()
                            && IMConstants.ZERO != creditNoteLineType.getInvoicePeriods().size()) {
                        baseItemDetails.setStartDate(creditNoteLineType.getInvoicePeriods().get(0).getStartDate().getValue());
                        baseItemDetails.setEndDate(creditNoteLineType.getInvoicePeriods().get(0).getEndDate().getValue());
                    }

                if(null != baseItemDetails.getStartDate() && null != baseItemDetails.getEndDate()) {
                    baseItemDetails.setNoOfDays(getDays(baseItemDetails.getStartDate(), baseItemDetails.getEndDate()));
                }

                //IM-40 : if grid Owners do not explicitly state the vat amount, in this case vat-amount has to be calculated based on
                // cbc:LineExtensionAmount currencyID="NOK">593.61</cbc:LineExtensionAmount and <cbc:Percent>25.00</cbc:Percent>
                if(taxAmount.equals(new Float("0.0")) && creditNoteLineType.getItem().getClassifiedTaxCategories()!=null && creditNoteLineType.getItem().getClassifiedTaxCategories().size()>0 && creditNoteLineType.getItem().getClassifiedTaxCategories().get(0)!=null
                        && creditNoteLineType.getItem().getClassifiedTaxCategories().get(0).getPercent()!=null && creditNoteLineType.getItem().getClassifiedTaxCategories().get(0).getPercent().getValue()!=null)
                {
                    baseItemDetails.setLineItemGrossAmount(Float.valueOf(creditNoteLineType.getLineExtensionAmount().getValue().toString()) +
                            (Float.valueOf(creditNoteLineType.getItem().getClassifiedTaxCategories().get(0).getPercent().getValue().toString())/100)*new Float(creditNoteLineType.getLineExtensionAmount().getValue().toString())* IMConstants.NEGATIVE);
                }
                else
                {
                    baseItemDetails.setLineItemGrossAmount((Float.valueOf(creditNoteLineType.getLineExtensionAmount().getValue().toString()) + taxAmount)* IMConstants.NEGATIVE);
                }
                baseItemDetailsList.add(baseItemDetails);
                sumGrossAmount +=baseItemDetails.getLineItemGrossAmount();
                sumOfVatAmount+=baseItemDetails.getVatInfo().getVatAmount();
                nettleie.setBaseItemDetails(baseItemDetailsList);
                nettleie.setMapOfVatSumOfGross(mapOfVatSumOfGross);
                nettleie.setSumOfNettAmount(Float.valueOf(creditNote.getLegalMonetaryTotal().getTaxExclusiveAmount().getValue().toString())*IMConstants.NEGATIVE);

                if (null == startDate) {
                    startDate = baseItemDetails.getStartDate();
                } else {
                    XMLGregorianCalendar startDate2 = baseItemDetails.getStartDate();
                    if (null != startDate2 && startDate.toGregorianCalendar().compareTo(startDate2.toGregorianCalendar()) > 0) {
                        startDate = startDate2;
                    }
                }
            }
                DecimalFormat df = new DecimalFormat("#.##");
                if(  df.format(sumGrossAmount).equals(df.format(nettleie.getInvoiceSummary().getInvoiceTotals().getGrossAmount())))
                {
                    nettleie.setSumMatching(true);
                }
                else
                {
                    nettleie.setSumMatching(false);
                }
                nettleie.setTotalVatAmount(sumOfVatAmount);
                nettleie.setCreditNote(true);
                if(startDate!=null) {
                String monthName =  Month.of(startDate.getMonth()).getDisplayName (TextStyle.FULL, new Locale("no","NO"));
                int year =   startDate.getYear();
                nettleie.setStartMonthAndYear(monthName + " " + year);
                }
        }  else {
                Invoice invoice = pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice();
                if(invoice!=null) {
                    nettleie.setReferenceNumber(invoice.getID().getValue().toString());
                    if(invoice.getDeliveries().get(0)!=null && invoice.getDeliveries().get(0).getDeliveryLocation()!=null &&
                            invoice.getDeliveries().get(0).getDeliveryLocation().getAddress()!=null &&
                            invoice.getDeliveries().get(0).getDeliveryLocation().getAddress().getStreetName()!=null)

                    nettleie.setFreeText(invoice.getDeliveries().get(0).getDeliveryLocation().getAddress().getStreetName().getValue());
                    nettleie.setDescription(null);
                    nettleie.setObjectId((Long.valueOf(invoice.getDeliveries().get(0).getDeliveryLocation().getID().getValue().toString())));
                    nettleie.setMeterId(invoice.getAccountingSupplierParty().getParty().getPartyLegalEntities().get(0).getCompanyID().getValue());
                    nettleie.setAnnualConsumption(0);
                    nettleie.setGridName(pdfAttachment.getFAKTURA().getAKTORNAVN());
                    invoiceSummary.getInvoiceTotals().setGrossAmount(Float.valueOf(pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice()
                            .getLegalMonetaryTotal().getTaxInclusiveAmount().getValue().toString()));

                    invoiceSummary.getInvoiceTotals().setOrigGrossAmount(Float.valueOf(pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice()
                            .getLegalMonetaryTotal().getTaxInclusiveAmount().getValue().toString()));

                    if(pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getTaxTotals()!=null && !pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getTaxTotals().isEmpty()
                            && pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getTaxTotals().get(0).getTaxSubtotals()!=null
                            && !pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getTaxTotals().get(0).getTaxSubtotals().isEmpty()
                            && pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getTaxTotals().get(0).getTaxSubtotals().get(0).getTaxAmount()!=null
                            &&  pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getTaxTotals().get(0).getTaxSubtotals().get(0).getTaxAmount().getValue()!=null) {
                        invoiceSummary.getInvoiceTotals().setVatTotalsAmount(Float.valueOf(pdfAttachment.getFAKTURA().getVedleggehfObj()
                                .getInvoice().getTaxTotals().get(0).getTaxSubtotals().get(0).getTaxAmount().getValue().toString()));
                    } else if(pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getTaxTotals()!=null && !pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getTaxTotals().isEmpty()
                            && pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getTaxTotals().get(0).getTaxAmount()!=null && pdfAttachment.getFAKTURA().getVedleggehfObj().getInvoice().getTaxTotals().get(0).getTaxAmount().getValue()!=null) {
                        invoiceSummary.getInvoiceTotals().setVatTotalsAmount(Float.valueOf(pdfAttachment.getFAKTURA().getVedleggehfObj()
                                .getInvoice().getTaxTotals().get(0).getTaxAmount().getValue().toString()));
                    }

                 /*   invoiceSummary.getInvoiceTotals().setVatTotalsAmount(Float.valueOf(pdfAttachment.getFAKTURA().getVedleggehfObj()
                            .getInvoice().getTaxTotals().get(0).getTaxSubtotals().get(0).getTaxAmount().getValue().toString()));*/

                    nettleie.setInvoiceSummary(invoiceSummary);

                    List<InvoiceLineType> invoiceLineTypeList = invoice.getInvoiceLines();
                    Float sumGrossAmount = 0.0f;
                    Float sumOfVatAmount = 0.0f;
                    Map<Float,Float> mapOfVatSumOfGross = new HashMap<Float,Float>();
                    XMLGregorianCalendar startDate = null;
                    int noOfDays = 0;
                    for (InvoiceLineType invoiceLineType : invoiceLineTypeList) {
                        baseItemDetails = new BaseItemDetails();
                        baseItemDetails.setDescription(invoiceLineType.getItem().getName().getValue().toString());
                        baseItemDetails.setQuantityInvoiced(Float.valueOf(invoiceLineType.getInvoicedQuantity().getValue().toString()));
                        baseItemDetails.setUnitOfMeasure(invoiceLineType.getInvoicedQuantity().getUnitCode());
                        baseItemDetails.setUnitPrice(Float.valueOf(invoiceLineType.getPrice().getPriceAmount().getValue().toString()));
                        baseItemDetails.setPriceDenomination(invoiceLineType.getPrice().getPriceAmount().getCurrencyID());
                        baseItemDetails.setAttachmentFormat(pdfAttachment.getFAKTURA().getVEDLEGGFORMAT());

                        List<TaxCategoryType> categoryTypeList = invoiceLineType.getItem().getClassifiedTaxCategories();
                        BigDecimal vat = null;
                        for(TaxCategoryType taxCategoryType: categoryTypeList) {
                            logger.debug("taxScheme " + taxCategoryType.getTaxScheme().getID().getValue()+ " vat "+ taxCategoryType.getPercent().getValue());
                            if("VAT".equals(taxCategoryType.getTaxScheme().getID().getValue())) {
                                vat = taxCategoryType.getPercent().getValue();
                                break;
                            }
                        }
                        baseItemDetails.setVatRate(Float.valueOf(vat.toString()));
                        baseItemDetails.setLineExtensionAmount( Float.valueOf(invoiceLineType.getLineExtensionAmount().getValue().toString()));
                        if(baseItemDetails.getVatInfo() == null) {
                        VatInfo vatInfo = new VatInfo();
                        if(invoiceLineType.getTaxTotals()!=null && invoiceLineType.getTaxTotals().size()>0) {
                        vatInfo.setVatAmount(Float.valueOf(invoiceLineType.getTaxTotals().get(0).getTaxAmount().getValue().toString()));
                        }
                        baseItemDetails.setVatInfo(vatInfo);
                        }
                        if(mapOfVatSumOfGross.containsKey(baseItemDetails.getVatRate()))
                        {
                           float taxAmt = mapOfVatSumOfGross.get(baseItemDetails.getVatRate());
                            taxAmt += baseItemDetails.getLineExtensionAmount();//baseItemDetails.getVatInfo().getVatAmount();
                            mapOfVatSumOfGross.put(baseItemDetails.getVatRate(),taxAmt);
                        } else {

                            mapOfVatSumOfGross.put(baseItemDetails.getVatRate(),baseItemDetails.getLineExtensionAmount());
                        }
                        if(null != vat) {
                            baseItemDetails.setUnitPriceGross(baseItemDetails.getUnitPrice() + ((vat.floatValue() / 100) * baseItemDetails.getUnitPrice()));
                        } else {
                            baseItemDetails.setUnitPriceGross(baseItemDetails.getUnitPrice());
                        }

                        if (null != invoiceLineType.getTaxTotals() && invoiceLineType.getTaxTotals().size() > 0 && null != invoiceLineType.getTaxTotals().get(0)) {
                            taxAmount = invoiceLineType.getTaxTotals().get(0).getTaxAmount().getValue().floatValue();
                        }
                            if (null != invoiceLineType.getInvoicePeriods()
                                    && IMConstants.ZERO != invoiceLineType.getInvoicePeriods().size()) {
                                baseItemDetails.setStartDate(invoiceLineType.getInvoicePeriods().get(0).getStartDate().getValue());
                                baseItemDetails.setEndDate(invoiceLineType.getInvoicePeriods().get(0).getEndDate().getValue());
                            }
                        if(null != baseItemDetails.getStartDate() && null != baseItemDetails.getEndDate()) {
                            baseItemDetails.setNoOfDays(getDays(baseItemDetails.getStartDate(), baseItemDetails.getEndDate()));
                            noOfDays = (int)baseItemDetails.getNoOfDays();
                        }
                        //IM-40 : if grid Owners do not explicitly state the vat amount, in this case vat-amount has to be calculated based on
                        // cbc:LineExtensionAmount currencyID="NOK">593.61</cbc:LineExtensionAmount and <cbc:Percent>25.00</cbc:Percent>
                        if(taxAmount.equals(new Float("0.0")) && invoiceLineType.getItem().getClassifiedTaxCategories()!=null && invoiceLineType.getItem().getClassifiedTaxCategories().size()>0 && invoiceLineType.getItem().getClassifiedTaxCategories().get(0)!=null
                                && invoiceLineType.getItem().getClassifiedTaxCategories().get(0).getPercent()!=null && invoiceLineType.getItem().getClassifiedTaxCategories().get(0).getPercent().getValue()!=null)
                        {
                            baseItemDetails.setLineItemGrossAmount(Float.valueOf(invoiceLineType.getLineExtensionAmount().getValue().toString()) +
                                    (Float.valueOf(invoiceLineType.getItem().getClassifiedTaxCategories().get(0).getPercent().getValue().toString())/100)*new Float(invoiceLineType.getLineExtensionAmount().getValue().toString()));

                        }
                        else
                        {
                            baseItemDetails.setLineItemGrossAmount(Float.valueOf(invoiceLineType.getLineExtensionAmount().getValue().toString()) + taxAmount);
                        }
                        baseItemDetailsList.add(baseItemDetails);
                        sumGrossAmount +=baseItemDetails.getLineItemGrossAmount();
                        sumOfVatAmount+=baseItemDetails.getVatInfo().getVatAmount();
                        if (null == startDate && baseItemDetails.getStartDate()!=null ) {
                            startDate = baseItemDetails.getStartDate();
                        } else {
                            XMLGregorianCalendar startDate2 = baseItemDetails.getStartDate();
                            if (null != startDate2 && startDate.toGregorianCalendar().compareTo(startDate2.toGregorianCalendar()) > 0) {
                                startDate = startDate2;
                            }
                        }

                    }
                    DecimalFormat df = new DecimalFormat("#.##");
                    if(  df.format(sumGrossAmount).equals(df.format(nettleie.getInvoiceSummary().getInvoiceTotals().getGrossAmount())))
                    {
                        nettleie.setSumMatching(true);
                    }
                    else
                    {
                        nettleie.setSumMatching(false);
                    }
                    nettleie.setMapOfVatSumOfGross(mapOfVatSumOfGross);
                    nettleie.setBaseItemDetails(baseItemDetailsList);
                    nettleie.setSumOfNettAmount(Float.valueOf(invoice.getLegalMonetaryTotal().getTaxExclusiveAmount().getValue().toString()));
                    nettleie.setTotalVatAmount(sumOfVatAmount);
                    if(noOfDays<30) {
                    String monthName =  Month.of(startDate.getMonth()).getDisplayName (TextStyle.FULL, new Locale("no","NO"));
                    int year =   startDate.getYear();
                    nettleie.setStartMonthAndYear(monthName + " " + year);
                    }
                }
            }

        }
        return nettleie;
    }

    private Nettleie createE2BEntry(Attachment pdfAttachment) {
        logger.debug("createE2BEntry " + pdfAttachment.getFAKTURA().getMAALEPUNKT());
        Nettleie nettleie = new Nettleie();
        nettleie.setFakturanr(pdfAttachment.getFAKTURA().getFAKTURANR());
        no.fjordkraft.im.if320.models.Invoice invoice = pdfAttachment.getFAKTURA().getVedlegge2BObj().getInvoice();
        nettleie.setReferenceNumber(String.valueOf(invoice.getInvoiceHeader().getInvoiceNumber()));
        nettleie.setFreeText(invoice.getInvoiceHeader().getEnergyHeader().getMeterLocation());
        nettleie.setDescription(null);
        nettleie.setObjectId(invoice.getInvoiceHeader().getEnergyHeader().getObjectId());
        nettleie.setMeterId(invoice.getInvoiceHeader().getEnergyHeader().getMeterId());
        nettleie.setAnnualConsumption(0);
        nettleie.setGridName(invoice.getInvoiceHeader().getEnergyHeader().getLdc1());
        List<BaseItemDetails> baseItemDetailsList = invoice.getInvoiceDetails().getBaseItemDetails();
            Map<Float,Float> mapOfVatSumOfGross = new HashMap<Float,Float>();
            float sumOfVatAmount = 0.0f;
        XMLGregorianCalendar startDate = null;
        for (BaseItemDetails baseItemDetails : baseItemDetailsList) {
            if(!"Beløpet overføres din Kraftleverandør".equals(baseItemDetails.getDescription().trim()))  {
            List<Ref> refList = baseItemDetails.getRef();
            baseItemDetails.setUnitPriceGross(baseItemDetails.getUnitPrice());
            baseItemDetails.setAttachmentFormat(pdfAttachment.getFAKTURA().getVEDLEGGFORMAT());
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

            baseItemDetails.setVatRate(Float.valueOf(Math.round(baseItemDetails.getVatInfo().getVatAmount()/baseItemDetails.getLineItemAmount()*100)));
            baseItemDetails.setLineExtensionAmount( Float.valueOf(baseItemDetails.getLineItemAmount()));
            baseItemDetails.getVatInfo().setVatAmount(invoice.getInvoiceSummary().getInvoiceTotals().getVatTotalsAmount());
            sumOfVatAmount+=baseItemDetails.getVatInfo().getVatAmount();
            if(mapOfVatSumOfGross.containsKey(baseItemDetails.getVatRate()))
            {
                float lineExtensionAmt = mapOfVatSumOfGross.get(baseItemDetails.getVatRate());
                lineExtensionAmt += baseItemDetails.getLineExtensionAmount();
                mapOfVatSumOfGross.put(baseItemDetails.getVatRate(),lineExtensionAmt);
            } else {

                mapOfVatSumOfGross.put(baseItemDetails.getVatRate(),baseItemDetails.getLineExtensionAmount());
            }
            if (null == startDate && baseItemDetails.getStartDate()!=null ) {
                startDate = baseItemDetails.getStartDate();
            } else {
                XMLGregorianCalendar startDate2 = baseItemDetails.getStartDate();
                if (null != startDate2 && startDate.toGregorianCalendar().compareTo(startDate2.toGregorianCalendar()) > 0) {
                    startDate = startDate2;
                }
            }
        }
        }
        nettleie.setMapOfVatSumOfGross(mapOfVatSumOfGross);
        nettleie.setBaseItemDetails(invoice.getInvoiceDetails().getBaseItemDetails());
        invoice.getInvoiceSummary().getInvoiceTotals().setOrigGrossAmount(invoice.getInvoiceSummary().getInvoiceTotals().getGrossAmount());
        nettleie.setInvoiceSummary(invoice.getInvoiceSummary());
        if(invoice.getInvoiceSummary().getInvoiceTotals().getLineItemTotalsAmount()!=0.0f)
             nettleie.setSumOfNettAmount(invoice.getInvoiceSummary().getInvoiceTotals().getLineItemTotalsAmount());
        else
            nettleie.setSumOfNettAmount(invoice.getInvoiceSummary().getInvoiceTotals().getNetAmount());
        nettleie.setTotalVatAmount(sumOfVatAmount);
        String monthName =  Month.of(startDate.getMonth()).getDisplayName (TextStyle.FULL, new Locale("no","NO"));
        int year =   startDate.getYear();
        nettleie.setStartMonthAndYear(monthName + " " + year);
        return nettleie;
    }



    private long getDays(XMLGregorianCalendar startDate, XMLGregorianCalendar endDate){

        LocalDate localStartDate = LocalDate.of(startDate.getYear(),startDate.getMonth(),startDate.getDay());
        LocalDate localEndDate = LocalDate.of(endDate.getYear(),endDate.getMonth(),endDate.getDay());
        long days = ChronoUnit.DAYS.between(localStartDate,localEndDate);
        return days;
    }
}
