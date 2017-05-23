package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.if320.models.TransactionGroup;
import no.fjordkraft.im.model.*;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.repository.TransactionGroupRepository;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.collections4.map.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;
import java.util.*;

/**
 * Created by miles on 5/19/2017.
 */
@Service
@PreprocessorInfo(order=6)
public class TransactionGroupPreprocessor  extends BasePreprocessor{

    @Autowired
    TransactionGroupRepository transactionGroupRepository;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {
        Map<Long, Transaction> kraftTransactions = new HashMap<Long, Transaction>();
        Map<NettTransaction, Transaction> nettTransactions = new HashMap<NettTransaction, Transaction>();
        Map<String, Transaction> diverseRabatter = new HashMap<String, Transaction>();

        List<Transaction> transactions = request.getStatement().getTransactions().getTransaction();
        List<Attachment> attachments = request.getStatement().getAttachments().getAttachment();

        TransactionGroup transactionGroup = new TransactionGroup();
        List<Transaction> processedTransaction = new ArrayList<Transaction>();
        List<Distribution> distributions = new ArrayList<Distribution>();
        Distribution distribuion = null;
        String referenceNumber = null;
        Long meter = null;
        boolean meterFound = false;
        NettTransaction nettTransaction = null;
        Transaction existingTransactionInMap = null;

        Iterator mapIterator = null;
        List<no.fjordkraft.im.model.TransactionGroup> diverseList = transactionGroupRepository.queryTransactionGroupByName(IMConstants.DIVERSE);
        List<no.fjordkraft.im.model.TransactionGroup> rabatterList = transactionGroupRepository.queryTransactionGroupByName(IMConstants.RABATTER);

        for(Transaction singleTransaction:transactions){
            if (null != singleTransaction.getDistributions()) {
                distributions = singleTransaction.getDistributions().getDistribution();

                if(!distributions.isEmpty()){
                    distribuion = distributions.get(0);
                    if (IMConstants.KRAFT.equals(distribuion.getName())) {
                        referenceNumber = singleTransaction.getReference();

                        if (null != referenceNumber) {
                            for (Attachment singleAttachment : attachments) {
                                if (singleAttachment.getFAKTURA().getFAKTURANR().equals(referenceNumber)) {
                                    meter = singleAttachment.getFAKTURA().getMAALEPUNKT();
                                    meterFound = true;
                                    break;
                                }
                            }
                            if (meterFound && kraftTransactions.isEmpty()) {
                                kraftTransactions.put(meter, createTransactionEntry(false, singleTransaction, existingTransactionInMap));
                                meterFound = false;
                            } else if(meterFound){
                                existingTransactionInMap = kraftTransactions.get(meter);
                                if(null != existingTransactionInMap) {
                                    kraftTransactions.put(meter, createTransactionEntry(true, singleTransaction, existingTransactionInMap));
                                }else {
                                    kraftTransactions.put(meter, createTransactionEntry(false, singleTransaction, existingTransactionInMap));
                                }
                                meterFound = false;
                            }
                        }
                    } else if(IMConstants.NETT.equals(distribuion.getName())) {
                        if(nettTransactions.isEmpty()) {
                            nettTransaction = new NettTransaction();
                            nettTransaction.setTransactionCategory(singleTransaction.getTransactionCategory());
                            nettTransaction.setFreeText(singleTransaction.getFreeText());
                            nettTransactions.put(nettTransaction, createTransactionEntry(false, singleTransaction, existingTransactionInMap));
                        } else {
                            nettTransaction = new NettTransaction();
                            nettTransaction.setTransactionCategory(singleTransaction.getTransactionCategory());
                            nettTransaction.setFreeText(singleTransaction.getFreeText());

                            existingTransactionInMap = nettTransactions.get(nettTransaction);
                            if(null != existingTransactionInMap) {
                                nettTransactions.put(nettTransaction, createTransactionEntry(true, singleTransaction, existingTransactionInMap));
                            } else {
                                nettTransaction = new NettTransaction();
                                nettTransaction.setTransactionCategory(singleTransaction.getTransactionCategory());
                                nettTransaction.setFreeText(singleTransaction.getFreeText());
                                nettTransactions.put(nettTransaction, createTransactionEntry(false, singleTransaction, existingTransactionInMap));
                            }
                        }
                    }
                }
            }
        }

        for(Transaction transaction:transactions) {
            for(no.fjordkraft.im.model.TransactionGroup rabatter:rabatterList) {
                if(rabatter.getTransactionCategory().equals(transaction.getTransactionCategory())) {
                    diverseRabatter.put(IMConstants.RABATTER, createDiverseRabatterTransactionEntry(diverseRabatter, transaction, rabatter, IMConstants.RABATTER));
                }
            }
            for(no.fjordkraft.im.model.TransactionGroup diverse:diverseList) {
                if(diverse.getTransactionCategory().equals(transaction.getTransactionCategory())) {
                    diverseRabatter.put(IMConstants.DIVERSE, createDiverseRabatterTransactionEntry(diverseRabatter, transaction, diverse, IMConstants.DIVERSE));
                }
            }
        }

        mapIterator = kraftTransactions.entrySet().iterator();
        while(mapIterator.hasNext()) {
            Map.Entry pair = (Map.Entry)mapIterator.next();
            processedTransaction.add((Transaction) pair.getValue());
        }

        mapIterator = nettTransactions.entrySet().iterator();
        while(mapIterator.hasNext()) {
            Map.Entry pair = (Map.Entry) mapIterator.next();
            processedTransaction.add((Transaction) pair.getValue());
        }

        processedTransaction = groupProcessedTransaction(processedTransaction);
        mapIterator = diverseRabatter.entrySet().iterator();
        while(mapIterator.hasNext()) {
            Map.Entry pair = (Map.Entry) mapIterator.next();
            processedTransaction.add((Transaction) pair.getValue());
        }

        transactionGroup.setTransaction(processedTransaction);
        request.getStatement().setTransactionGroup(transactionGroup);
    }

    private Transaction createTransactionEntry(boolean update, Transaction singleTransaction, Transaction existingTransactionInMap) {
        Transaction transactionToMap = new Transaction();
        float newAamount;

        transactionToMap.setTransactionCategory(singleTransaction.getTransactionCategory().substring(3));
        transactionToMap.setFreeText(singleTransaction.getFreeText());
        if(update){
            newAamount = existingTransactionInMap.getAmountWithVat() + singleTransaction.getAmountWithVat();
            transactionToMap.setAmountWithVat(newAamount);
        }else {
            transactionToMap.setAmountWithVat(singleTransaction.getAmountWithVat());
        }
        return transactionToMap;
    }

    private Transaction createDiverseRabatterTransactionEntry(Map<String, Transaction> diverseRabatter, Transaction transaction,
                                                              no.fjordkraft.im.model.TransactionGroup group, String groupName) {
        Map<String, Transaction> existingElement = new HashMap<String, Transaction>();
        Transaction resultTransaction = new Transaction();
        float newAmount;
        Iterator existingElementIterator;

        resultTransaction.setTransactionCategory(group.getLabel());
        if(diverseRabatter.isEmpty()) {
            resultTransaction.setAmountWithVat(transaction.getAmountWithVat());
        } else {
            existingElement = (Map<String, Transaction>) diverseRabatter.get(groupName);
            if(null == existingElement || existingElement.isEmpty()) {
                resultTransaction.setAmountWithVat(transaction.getAmountWithVat());
            } else {
                existingElementIterator = existingElement.entrySet().iterator();
                while(existingElementIterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) existingElementIterator.next();
                    resultTransaction = (Transaction) entry.getValue();
                    newAmount = resultTransaction.getAmountWithVat() + transaction.getAmountWithVat();
                    resultTransaction.setAmountWithVat(newAmount);
                }
            }
        }
        return resultTransaction;
    }

    private List<Transaction> groupProcessedTransaction(List<Transaction> processedTransaction) {
        List<Transaction> transaction = new ArrayList<Transaction>();
        MultiValueMap multiValueMap = new MultiValueMap();
        Iterator mapIterator = null;
        List<Transaction> transactionList = null;

        for(Transaction individualTransaction:processedTransaction) {
            multiValueMap.put(individualTransaction.getFreeText(), individualTransaction);
        }

        mapIterator = multiValueMap.entrySet().iterator();
        while(mapIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) mapIterator.next();
            transactionList = (List<Transaction>) entry.getValue();
            transaction.addAll(transactionList);
        }
        return transaction;
    }
}
