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
import org.apache.commons.collections4.map.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by miles on 5/19/2017.
 */
@Service
@PreprocessorInfo(order=6)
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

                                    List<InvoiceLine120> invoiceLine120List = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceOrder().getInvoiceLine120();
                                    XMLGregorianCalendar startDate = null;
                                    XMLGregorianCalendar endDate = null;
                                    if(null != invoiceLine120List) {
                                        for (InvoiceLine120 invoiceLine120 : invoiceLine120List) {
                                            if (null == startDate) {
                                                startDate = invoiceLine120.getStartDate();
                                                endDate = invoiceLine120.getEndDate();
                                            } else {
                                                if (startDate.toGregorianCalendar().compareTo(invoiceLine120.getStartDate().toGregorianCalendar()) > 0) {
                                                    startDate = invoiceLine120.getStartDate();
                                                }
                                                if (endDate.toGregorianCalendar().compareTo(invoiceLine120.getEndDate().toGregorianCalendar()) < 0) {
                                                    endDate = invoiceLine120.getEndDate();
                                                }
                                            }
                                        }
                                    }
                                    transaction.setStartDate(startDate);
                                    transaction.setEndDate(endDate);
                                    attachment.getFAKTURA().setFreeText(transaction.getFreeText());
                                    attachment.getFAKTURA().setGrid(getGridConfigInfo(attachment.getFAKTURA().getVEDLEGGEMUXML()
                                            .getInvoice().getInvoiceOrder().getInvoiceOrderInfo110().getLDC1(),
                                            request.getEntity().getId()));
                                }
                            }
                            kraftTransaction.add(createTransactionEntry(transaction, IMConstants.KRAFT));
                            i++;
                        } else if (IMConstants.NETT.equals(distribuion.getName())) {
                            for(Attachment attachment:attachments) {
                                if(attachment.getFAKTURA().getFAKTURANR().equals(transaction.getReference())) {
                                    if(IMConstants.PDFEHF.equals(attachment.getFAKTURA().getVEDLEGGFORMAT())) {
                                        if(null != attachment.getFAKTURA().getVedleggehfObj()) {
                                            transaction.setStartDate(attachment.getFAKTURA().getVedleggehfObj().getInvoice().getInvoiceLines()
                                                    .get(0).getInvoicePeriods().get(0).getStartDate().getValue());

                                            invoiceLineSize = attachment.getFAKTURA().getVedleggehfObj().getInvoice().getInvoiceLines().size();

                                            transaction.setEndDate(attachment.getFAKTURA().getVedleggehfObj().getInvoice().getInvoiceLines()
                                                    .get(invoiceLineSize - 1).getInvoicePeriods().get(0).getEndDate().getValue());

                                            break;
                                        }
                                    } else if(IMConstants.PDFE2B.equals(attachment.getFAKTURA().getVEDLEGGFORMAT())) {

                                        transaction.setStartDate(attachment.getFAKTURA().getVedlegge2BObj().getInvoice().getInvoiceDetails()
                                        .getBaseItemDetails().get(0).getStartDate());

                                        invoiceLineSize = attachment.getFAKTURA().getVedlegge2BObj().getInvoice().getInvoiceDetails()
                                                .getBaseItemDetails().size();

                                        transaction.setEndDate(attachment.getFAKTURA().getVedlegge2BObj().getInvoice().getInvoiceDetails()
                                                .getBaseItemDetails().get(invoiceLineSize-1).getEndDate());

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

        for(Transaction individualTransaction:processedTransaction) {
            multiValueMap.put(individualTransaction.getFreeText(), individualTransaction);
        }

        int k = 1;
        mapIterator = multiValueMap.entrySet().iterator();
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
