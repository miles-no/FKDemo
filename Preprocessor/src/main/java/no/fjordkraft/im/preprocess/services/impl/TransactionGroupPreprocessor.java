package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.model.GridConfig;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.AuditLogService;
import no.fjordkraft.im.services.GridConfigService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.InvoiceLineType;
import org.apache.commons.collections4.map.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

/**
 * Created by miles on 5/19/2017.
 */
@Service
@PreprocessorInfo(order=7)
public class TransactionGroupPreprocessor extends BasePreprocessor {

    @Autowired
    GridConfigService gridConfigService;

    @Autowired
    AuditLogService auditLogService;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {
        try {
            List<Transaction> transactions = request.getStatement().getTransactions().getTransaction();
            List<Attachment> attachments = request.getStatement().getAttachments().getAttachment();
            List<Transaction> processedTransaction = new ArrayList<Transaction>();
            TransactionGroup transactionGroup = new TransactionGroup();

            List<Transaction> kraftTransaction = new ArrayList<Transaction>();
            List<Transaction> nettTransaction = new ArrayList<Transaction>();
            List<Distribution> distributions;
            Distribution distribuion = null;
            int invoiceLineSize;
            int i = 0;

            for (Transaction transaction : transactions) {
                if (null != transaction.getDistributions()) {
                    distributions = transaction.getDistributions().getDistribution();

                    if (!distributions.isEmpty()) {
                        distribuion = distributions.get(0);
                        if (IMConstants.KRAFT.equals(distribuion.getName())) {
                            for (Attachment attachment : attachments) {
                                if (transaction.getReference().equals(attachment.getFAKTURA().getFAKTURANR())) {

                                    List<InvoiceLine120> invoiceLine120List = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getInvoiceLine120();
                                    XMLGregorianCalendar startDate = null;
                                    XMLGregorianCalendar endDate = null;
                                    if(null != invoiceLine120List) {
                                        for (InvoiceLine120 invoiceLine120 : invoiceLine120List) {
                                            if (null == startDate) {
                                                startDate = invoiceLine120.getStartDate();
                                                endDate = invoiceLine120.getEndDate();
                                            } else {
                                                if (null != invoiceLine120.getStartDate() && startDate.toGregorianCalendar().compareTo(invoiceLine120.getStartDate().toGregorianCalendar()) > 0) {
                                                    startDate = invoiceLine120.getStartDate();
                                                }
                                                if (null != endDate && null!=invoiceLine120.getEndDate() &&  endDate.toGregorianCalendar().compareTo(invoiceLine120.getEndDate().toGregorianCalendar()) < 0) {
                                                    endDate = invoiceLine120.getEndDate();
                                                }
                                            }
                                        }
                                    }
                                    transaction.setStartDate(startDate);
                                    transaction.setEndDate(endDate);
                                    attachment.getFAKTURA().setFreeText(transaction.getFreeText());
                                    attachment.getFAKTURA().setGrid(getGridConfigInfo(attachment.getFAKTURA().getVEDLEGGEMUXML()
                                            .getInvoice().getInvoiceFinalOrder().getInvoiceOrderInfo110().getLDC1(),
                                            request.getEntity().getId()));
                                    attachment.setStartDate(startDate);
                                    attachment.setEndDate(endDate);
                                }
                            }
                            kraftTransaction.add(createTransactionEntry(transaction, IMConstants.KRAFT));
                            i++;
                        } else if (IMConstants.NETT.equals(distribuion.getName())) {
                            for(Attachment attachment:attachments) {
                                if(attachment.getFAKTURA().getFAKTURANR().equals(transaction.getReference())) {
                                    if(IMConstants.PDFEHF.equals(attachment.getFAKTURA().getVEDLEGGFORMAT())) {
                                        if(null != attachment.getFAKTURA().getVedleggehfObj()) {
                                            XMLGregorianCalendar startDate = null;
                                            XMLGregorianCalendar endDate = null;

                                            List<InvoiceLineType> invoiceLineTypeList = attachment.getFAKTURA().getVedleggehfObj().getInvoice().getInvoiceLines();
                                            if(null != invoiceLineTypeList) {
                                                for (InvoiceLineType invoiceLineType : invoiceLineTypeList) {
                                                    if(null!=invoiceLineType.getInvoicePeriods() && invoiceLineType.getInvoicePeriods().size() > 0)
                                                    {
                                                        if (null == startDate) {
                                                            startDate = invoiceLineType.getInvoicePeriods().get(0).getStartDate().getValue();
                                                            endDate = invoiceLineType.getInvoicePeriods().get(0).getEndDate().getValue();
                                                        } else {
                                                            XMLGregorianCalendar startDate2 = invoiceLineType.getInvoicePeriods().get(0).getStartDate().getValue();
                                                            XMLGregorianCalendar endDate2 = invoiceLineType.getInvoicePeriods().get(0).getEndDate().getValue();
                                                            if (null != startDate2 && startDate.toGregorianCalendar().compareTo(startDate2.toGregorianCalendar()) > 0) {
                                                                startDate = startDate2;
                                                            }
                                                            if (null != endDate2 && null != endDate && endDate.toGregorianCalendar().compareTo(endDate2.toGregorianCalendar()) < 0) {
                                                                endDate = endDate2;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            transaction.setStartDate(startDate);
                                            transaction.setEndDate(endDate);
                                            attachment.setStartDate(startDate);
                                            attachment.setEndDate(endDate);
                                            break;
                                        }
                                    } else if(IMConstants.PDFE2B.equals(attachment.getFAKTURA().getVEDLEGGFORMAT())) {

                                        List<BaseItemDetails> baseItemDetailsList = attachment.getFAKTURA().getVedlegge2BObj().getInvoice().getInvoiceDetails().getBaseItemDetails();
                                        XMLGregorianCalendar startDate = null;
                                        XMLGregorianCalendar endDate = null;
                                        if(null != baseItemDetailsList) {
                                            for (BaseItemDetails baseItemDetails : baseItemDetailsList) {
                                                if (null == startDate) {
                                                    startDate = baseItemDetails.getStartDate();
                                                    endDate = baseItemDetails.getEndDate();
                                                } else {
                                                    XMLGregorianCalendar startDate2 = baseItemDetails.getStartDate();
                                                    XMLGregorianCalendar endDate2 = baseItemDetails.getEndDate();
                                                    if (null!=startDate2 && startDate.toGregorianCalendar().compareTo(startDate2.toGregorianCalendar()) > 0) {
                                                        startDate = startDate2;
                                                    }
                                                    if (null != endDate2 && endDate.toGregorianCalendar().compareTo(endDate2.toGregorianCalendar()) < 0) {
                                                        endDate = endDate2;
                                                    }
                                                }
                                            }
                                        }
                                        transaction.setStartDate(startDate);
                                        transaction.setEndDate(endDate);
                                        attachment.setStartDate(startDate);
                                        attachment.setEndDate(endDate);
                                        break;
                                    }
                                }
                            }
                            nettTransaction.add(createTransactionEntry(transaction, IMConstants.NETT));
                            i++;
                        }
                    }
                }
            }
            processedTransaction.addAll(kraftTransaction);
            processedTransaction.addAll(nettTransaction);
            transactionGroup.setTransaction(groupProcessedTransaction(processedTransaction));
            transactionGroup.setTotalTransactions(i);
            request.getStatement().setTransactionGroup(transactionGroup);
            request.getStatement().setTotalVatAmount(IMConstants.NEGATIVE * request.getStatement().getTotalVatAmount());
        } catch (Exception ex) {
            throw new PreprocessorException("Failed in Transaction Group Pre-Processor with message: ",ex );
        }
    }



    private Transaction createTransactionEntry(Transaction transaction, String type) {
        Transaction resultTransaction = new Transaction();
        resultTransaction.setTransactionType(type);
        resultTransaction.setTransactionCategory(transaction.getTransactionCategory().substring(3));
        resultTransaction.setFreeText(transaction.getFreeText());
        resultTransaction.setAmountWithVat(transaction.getAmountWithVat()* IMConstants.NEGATIVE);
        resultTransaction.setStartDate(transaction.getStartDate());
        resultTransaction.setEndDate(transaction.getEndDate());
        resultTransaction.setReference(transaction.getReference());
        return resultTransaction;
    }

    private List<Transaction> groupProcessedTransaction(List<Transaction> processedTransaction) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        MultiValueMap multiValueMap = new MultiValueMap();
        Iterator mapIterator = null;
        List<Transaction> transactionList = null;
        //int i = 1;
        Map<String,List<Transaction>> groupTransactions = new HashMap<>();

        for(Transaction individualTransaction:processedTransaction) {
            //multiValueMap.put(individualTransaction.getFreeText(), individualTransaction);
            if(groupTransactions.containsKey(individualTransaction.getFreeText().trim())){
                List<Transaction> tlist =  groupTransactions.get(individualTransaction.getFreeText());
                if(IMConstants.KRAFT.equals(individualTransaction.getTransactionType())){
                    tlist.add(0,individualTransaction);
                } else {
                    tlist.add(individualTransaction);
                }
                groupTransactions.put(individualTransaction.getFreeText(),tlist);
            } else {
                List<Transaction> tlist = new ArrayList<>();
                tlist.add(individualTransaction);
                groupTransactions.put(individualTransaction.getFreeText().trim(),tlist);
            }
        }

        int k = 1;
        mapIterator = groupTransactions.entrySet().iterator();
        while(mapIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) mapIterator.next();
            transactionList = (List<Transaction>) entry.getValue();
            for(Transaction transaction:transactionList) {
                transaction.setTransactionSequence(k++);
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    private Grid getGridConfigInfo(String ldc1, Long id) {
        Grid grid = new Grid();

        GridConfig gridConfig = gridConfigService.getGridConfigByBrand(ldc1.toUpperCase());
        if(null != gridConfig) {
            grid.setName(gridConfig.getGridLabel());
            grid.setEmail(gridConfig.getEmail());
            grid.setTelephone(gridConfig.getPhone());
        } else {
            String errorMessage = "Grid not found: " + ldc1;
            auditLogService.saveAuditLog(id, StatementStatusEnum.PRE_PROCESSING.getStatus(), errorMessage, IMConstants.WARNING);
        }
        return grid;
    }

    public void setGridConfigService(GridConfigService gridConfigService) {
        this.gridConfigService = gridConfigService;
    }

    public void setAuditLogService(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }
}
