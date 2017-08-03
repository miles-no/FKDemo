package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.model.GridConfig;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.impl.AuditLogServiceImpl;
import no.fjordkraft.im.services.impl.GridConfigServiceImpl;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.collections4.map.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by miles on 5/19/2017.
 */
@Service
@PreprocessorInfo(order=6)
public class TransactionGroupPreprocessor  extends BasePreprocessor{

    @Autowired
    GridConfigServiceImpl gridConfigService;

    @Autowired
    AuditLogServiceImpl auditLogService;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {
        try {
            List<Transaction> transactions = request.getStatement().getTransactions().getTransaction();
            List<Attachment> attachments = request.getStatement().getAttachments().getAttachment();
            List<Transaction> processedTransaction = new ArrayList<Transaction>();
            TransactionGroup transactionGroup = new TransactionGroup();

            List<Transaction> kraftTransaction = new ArrayList<Transaction>();
            List<Transaction> nettTransaction = new ArrayList<Transaction>();
            List<Distribution> distributions = new ArrayList<Distribution>();
            Distribution distribuion = null;
            int i = 0;

            for (Transaction transaction : transactions) {
                if (null != transaction.getDistributions()) {
                    distributions = transaction.getDistributions().getDistribution();

                    if (!distributions.isEmpty()) {
                        distribuion = distributions.get(0);
                        if (IMConstants.KRAFT.equals(distribuion.getName())) {
                            for (Attachment attachment : attachments) {
                                if (transaction.getReference().equals(attachment.getFAKTURA().getFAKTURANR())) {

                                    ReadingInfo111 readingInfo111 = attachment.getFAKTURA().getVEDLEGGEMUXML()
                                            .getInvoice().getInvoiceOrder().getReadingInfo111();
                                    if (null != readingInfo111) {
                                        transaction.setStartDate(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice()
                                                .getInvoiceOrder().getReadingInfo111().getStartDate());

                                        transaction.setEndDate(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice()
                                                .getInvoiceOrder().getReadingInfo111().getEndDate());
                                    }
                                    attachment.getFAKTURA().setFreeText(transaction.getFreeText());
                                    attachment.getFAKTURA().setGrid(getGridConfigInfo(attachment.getFAKTURA().getVEDLEGGEMUXML()
                                            .getInvoice().getInvoiceOrder().getInvoiceOrderInfo110().getLDC1(),
                                            request.getEntity().getId(), request.getEntity().getInvoiceNumber()));
                                }
                            }
                            kraftTransaction.add(createTransactionEntry(transaction, IMConstants.KRAFT));
                            i++;
                        } else if (IMConstants.NETT.equals(distribuion.getName())) {
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
            if(i > 10) {
                request.getStatement().setTotalAttachment(attachments.size() + 1);
            } else {
                request.getStatement().setTotalAttachment(attachments.size());
            }
        } catch (Exception ex) {
            throw new PreprocessorException("Failed in Transaction Group Pre-Processor with message: " + ex.getMessage());
        }
    }

    private Transaction createTransactionEntry(Transaction transaction, String type) {
        Transaction resultTransaction = new Transaction();
        resultTransaction.setTransactionType(type);
        resultTransaction.setTransactionCategory(transaction.getTransactionCategory().substring(3));
        resultTransaction.setFreeText(transaction.getFreeText());
        resultTransaction.setAmountWithVat(transaction.getAmountWithVat()*IMConstants.NEGATIVE);
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

    private Grid getGridConfigInfo(String ldc1, Long id, String invoiceNo) {
        Grid grid = new Grid();

        GridConfig gridConfig = gridConfigService.getGridConfigByBrand(ldc1);
        if(null != gridConfig) {
            grid.setName(gridConfig.getGridName());
            grid.setEmail(gridConfig.getEmail());
            grid.setTelephone(gridConfig.getPhone());
        } else {
            String errorMessage = "Grid not found: " + ldc1;
            auditLogService.saveAuditLog(id, StatementStatusEnum.PRE_PROCESSING.getStatus(), errorMessage, IMConstants.WARNING);
        }
        return grid;
    }
}
