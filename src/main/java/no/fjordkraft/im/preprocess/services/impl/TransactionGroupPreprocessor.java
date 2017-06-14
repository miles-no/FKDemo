package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.collections4.map.MultiValueMap;
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

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {
        List<Transaction> transactions = request.getStatement().getTransactions().getTransaction();
        List<Attachment> attachments = request.getStatement().getAttachments().getAttachment();
        List<Transaction> processedTransaction = new ArrayList<Transaction>();
        TransactionGroup transactionGroup = new TransactionGroup();

        List<Transaction> kraftTransaction = new ArrayList<Transaction>();
        List<Transaction> nettTransaction =  new ArrayList<Transaction>();
        List<Distribution> distributions = new ArrayList<Distribution>();
        Distribution distribuion = null;

        for(Transaction transaction:transactions) {
            if (null != transaction.getDistributions()) {
                distributions = transaction.getDistributions().getDistribution();

                if (!distributions.isEmpty()) {
                    distribuion = distributions.get(0);
                    if (IMConstants.KRAFT.equals(distribuion.getName())) {
                        for(Attachment attachment:attachments) {
                            if(transaction.getReference().equals(attachment.getFAKTURA().getFAKTURANR())) {

                                ReadingInfo111 readingInfo111 = attachment.getFAKTURA().getVEDLEGGEMUXML()
                                        .getInvoice().getInvoiceOrder().getReadingInfo111();
                                if(null != readingInfo111) {
                                    transaction.setStartDate(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice()
                                            .getInvoiceOrder().getReadingInfo111().getStartDate());

                                    transaction.setEndDate(attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice()
                                            .getInvoiceOrder().getReadingInfo111().getEndDate());
                                }
                            }
                        }
                        kraftTransaction.add(createTransactionEntry(transaction, IMConstants.KRAFT));
                    } else if(IMConstants.NETT.equals(distribuion.getName())) {
                        nettTransaction.add(createTransactionEntry(transaction, IMConstants.NETT));
                    }
                }
            }
        }
        processedTransaction.addAll(kraftTransaction);
        processedTransaction.addAll(nettTransaction);
        transactionGroup.setTransaction(groupProcessedTransaction(processedTransaction));
        request.getStatement().setTransactionGroup(transactionGroup);
        request.getStatement().setTotalVatAmount(IMConstants.NEGATIVE*request.getStatement().getTotalVatAmount());
    }

    private Transaction createTransactionEntry(Transaction transaction, String type) {
        Transaction resultTransaction = new Transaction();
        resultTransaction.setTransactionType(type);
        resultTransaction.setTransactionCategory(transaction.getTransactionCategory().substring(3));
        resultTransaction.setFreeText(transaction.getFreeText());
        resultTransaction.setAmountWithVat(transaction.getAmountWithVat()*IMConstants.NEGATIVE);
        resultTransaction.setStartDate(transaction.getStartDate());
        resultTransaction.setEndDate(transaction.getEndDate());
        return resultTransaction;
    }

    private List<Transaction> groupProcessedTransaction(List<Transaction> processedTransaction) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        MultiValueMap multiValueMap = new MultiValueMap();
        Iterator mapIterator = null;
        List<Transaction> transactionList = null;
        int i = 1;

        for(Transaction individualTransaction:processedTransaction) {
            multiValueMap.put(individualTransaction.getFreeText(), individualTransaction);
        }

        mapIterator = multiValueMap.entrySet().iterator();
        while(mapIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) mapIterator.next();
            transactionList = (List<Transaction>) entry.getValue();
            for(Transaction transaction:transactionList) {
                transaction.setTransactionSequence(i++);
                transactions.add(transaction);
            }
        }
        return transactions;
    }
}
