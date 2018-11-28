package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by bhavi on 6/14/2018.
 */

@Service
@PreprocessorInfo(order = 95)
public class MapToSameMeterPreprocessor extends BasePreprocessor {

    private static final Logger logger = LoggerFactory.getLogger(MapToSameMeterPreprocessor.class);

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {

        Statement stmt = request.getStatement();
        String brand = request.getEntity().getSystemBatchInput().getBrand();
        if (request.getStatement().getLegalPartClass().equals("Organization") && (brand.equals("FKAS") || brand.equals("TKAS"))) {

            Map<Long, Attachment> meterAttachmentMap = new LinkedHashMap<>();
            List<Attachment> attachmentList = stmt.getAttachments().getAttachment();
            for (Attachment attachment : attachmentList) {
                if((attachment.getDisplayStromData()!=null && attachment.getDisplayStromData()) || attachment.isOnlyGrid())
                {
                    long meterId = attachment.getFAKTURA().getMAALEPUNKT();

                    if (meterAttachmentMap.containsKey(meterId)) {
                        logger.debug("Statement "+ request.getEntity().getId() + " Invoice number "+ request.getEntity().getInvoiceNumber() + " meter " + meterId + " exists ");
                        Attachment attachmentFromMap = meterAttachmentMap.get(meterId);
                        //copyStromInvoiceToAttachmentMap(attachmentFromMap, attachment);
                        addTransactionAmounts(attachmentFromMap, attachment);
                        combineNetteleie(attachmentFromMap, attachment);
                        if (attachment.getDisplayStromData()) {
                            InvoiceOrder invoiceOrder = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder();
                            invoiceOrder.setTransactionName(attachment.getTransactionName());
                            invoiceOrder.setInvoiceNo(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getMainInvoiceInfo101().getInvoiceNo());
                            attachmentFromMap.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceOrder().add(invoiceOrder);
                            attachmentFromMap.setOnlyGrid(false);
                        }

                    } else {
                        if (attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceOrder() != null && attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceOrder().size() > 0) {
                            Nettleie nettleie = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie();
                            if (nettleie != null && nettleie.getFakturanr()!=null/*&& nettleie.getBaseItemDetails() != null && nettleie.getBaseItemDetails().size() > 0*/) {
                                nettleie.setGrid(attachment.getFAKTURA().getGrid());
                                attachment.getFAKTURA().getNettleieList().add(nettleie);
                                attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setNettleie(null);
                            }
                        }
                    /*if(attachment.isOnlyGrid() && null != attachment.getFAKTURA().getVEDLEGGEMUXML() && null !=attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice() &&
                            null != attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceOrder()) {
                        attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceOrder().clear();
                    }*/
                        if (attachment.getDisplayStromData()) {
                            InvoiceOrder invoiceOrder = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder();
                            invoiceOrder.setTransactionName(attachment.getTransactionName());
                            invoiceOrder.setInvoiceNo(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getMainInvoiceInfo101().getInvoiceNo());
                        }
                        logger.debug("Statement "+ request.getEntity().getId() + " Invoice number "+ request.getEntity().getInvoiceNumber() + " meter " + meterId + " does not exists ");
                        meterAttachmentMap.put(meterId, attachment);
                    }
                }
            }
            List<Attachment> newAttachmentList = new ArrayList<>();
            Set<Map.Entry<Long, Attachment>> entrySet = meterAttachmentMap.entrySet();
            for (Map.Entry<Long, Attachment> entries : entrySet) {
                Attachment attachment = entries.getValue();
                int index = 1;
                for (InvoiceOrder invoiceOrder : attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceOrder()) {
                    invoiceOrder.setSequenceNumber(index++);
                }
                index = 1;
                for (Nettleie nettleie : attachment.getFAKTURA().getNettleieList()) {
                    nettleie.setSequenceNumber(index++);
                }
                newAttachmentList.add(attachment);
            }
            stmt.getAttachments().setAttachment(newAttachmentList);

        }
    }

    //add transaction summary and belop and vat sum
    private void addTransactionAmounts(Attachment attachmentFromMap,Attachment attachment){
        //sum with vat

        attachmentFromMap.setSumInklMVA((attachmentFromMap.getSumInklMVA()!=null?attachmentFromMap.getSumInklMVA():0)+
                (attachment.getSumInklMVA()!=null?attachment.getSumInklMVA():0));
        //sum without vat
        attachmentFromMap.setSumOfTransactions((attachmentFromMap.getSumOfTransactions()!=null ?attachmentFromMap.getSumOfTransactions():0)
                + (attachment.getSumOfTransactions()!=null ?attachment.getSumOfTransactions():0) );

        //sum total without vat for strom
        //IM-129 - added null check as not sure this field is used in template or not.No reference found in new template of organization.
       // In future it should be removed from template and code as well.
        if(attachmentFromMap.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getMainInvoiceInfo101()!=null) {
               attachmentFromMap.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getMainInvoiceInfo101().setNetPrintet(
               attachmentFromMap.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getMainInvoiceInfo101().getNetPrintet()
               + attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getMainInvoiceInfo101().getNetPrintet());
        }
        Map<Double,TransactionSummary> newTransactionSummaryToAdd = new HashMap<Double,TransactionSummary>();
        for(TransactionSummary fromMapTranSummary :attachmentFromMap.getTransactionSummary())     {
            newTransactionSummaryToAdd.put(fromMapTranSummary.getMvaValue(),fromMapTranSummary);
        }
        for(TransactionSummary transactionSummaryFromMap : attachmentFromMap.getTransactionSummary()) {
            for(TransactionSummary transactionSummary : attachment.getTransactionSummary()) {
                if(transactionSummaryFromMap.getMvaValue() == transactionSummary.getMvaValue()) {
                    transactionSummaryFromMap.setSumOfBelop(transactionSummaryFromMap.getSumOfBelop() + transactionSummary.getSumOfBelop()); // is computed vat amount
                    //sum amount for wich vat to be calculated
                    transactionSummaryFromMap.setSumOfNettStrom(transactionSummaryFromMap.getSumOfNettStrom() + transactionSummary.getSumOfNettStrom()); // is amount on which to apply vatrate
                    //total vat for vat rate
                    transactionSummaryFromMap.setTotalVatAmount(transactionSummaryFromMap.getTotalVatAmount()+ transactionSummary.getTotalVatAmount());
                    newTransactionSummaryToAdd.put(transactionSummaryFromMap.getMvaValue(),transactionSummaryFromMap);
                } else {
                    if(!newTransactionSummaryToAdd.containsKey(transactionSummary.getMvaValue()))
                    {
                        newTransactionSummaryToAdd.put(transactionSummary.getMvaValue(),transactionSummary);
                    }
                }
            }
        }

        attachmentFromMap.setTransactionSummary(new ArrayList<TransactionSummary>());
        for(TransactionSummary transactionSummary : newTransactionSummaryToAdd.values()){

            attachmentFromMap.getTransactionSummary().add(transactionSummary);
        }
    }

    private void combineNetteleie(Attachment attachmentFromMap,Attachment attachment){
        Nettleie nettleie = null;

        if(null != attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice() && null != attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder()) {
            nettleie = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie();
            if (nettleie != null && nettleie.getBaseItemDetails() != null && nettleie.getBaseItemDetails().size() > 0) {
                nettleie.setGrid(attachment.getFAKTURA().getGrid());
                attachmentFromMap.getFAKTURA().getNettleieList().add(nettleie);
                attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setNettleie(null);
            }
        }
    }

    private void copyStromInvoiceToAttachmentMap(Attachment attachmentFromMap, Attachment attachment) {
        /*if(attachmentFromMap.isOnlyGrid() && !attachment.isOnlyGrid()) {
            List<Nettleie> nettleieList = attachmentFromMap.getFAKTURA().getNettleieList();
            if(null != attachmentFromMap.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceOrder() && attachmentFromMap.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceOrder().size() >0){

            }
        }*/
        Attachment temp = attachmentFromMap;
        attachmentFromMap = attachment;
        attachment = temp;

        attachmentFromMap.getFAKTURA().getNettleieList().addAll(attachment.getFAKTURA().getNettleieList());
        if(null != attachmentFromMap.getFAKTURA().getVEDLEGGEMUXML().getInvoice() && null != attachmentFromMap.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceOrder()
                && attachmentFromMap.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceOrder().size() > 0) {
            attachmentFromMap.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().setNettleie(null);
        }

    }

}
