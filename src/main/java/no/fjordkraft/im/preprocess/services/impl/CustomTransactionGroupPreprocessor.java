package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.if320.models.Transaction;
import no.fjordkraft.im.if320.models.TransactionGroup;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.repository.TransactionGroupRepository;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by miles on 5/23/2017.
 */
@Service
@PreprocessorInfo(order=7)
public class CustomTransactionGroupPreprocessor  extends BasePreprocessor {

    @Autowired
    TransactionGroupRepository transactionGroupRepository;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {
        Map<String, Transaction> diverseRabatter = new HashMap<String, Transaction>();

        List<Transaction> transactions = request.getStatement().getTransactions().getTransaction();
        List<no.fjordkraft.im.model.TransactionGroup> diverseList = transactionGroupRepository.queryTransactionGroupByName(IMConstants.DIVERSE);
        List<no.fjordkraft.im.model.TransactionGroup> rabatterList = transactionGroupRepository.queryTransactionGroupByName(IMConstants.RABATTER);
        List<Transaction> processedTransaction = request.getStatement().getTransactionGroup().getTransaction();
        TransactionGroup transactionGroup = new TransactionGroup();
        Iterator mapIterator = null;
        int totalTransactions = request.getStatement().getTransactionGroup().getTotalTransactions();


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
        Transaction transaction;
        mapIterator = diverseRabatter.entrySet().iterator();
        while(mapIterator.hasNext()) {
            Map.Entry pair = (Map.Entry) mapIterator.next();
            transaction = new Transaction();
            transaction = (Transaction) pair.getValue();
            transaction.setTransactionSequence(++totalTransactions);
            processedTransaction.add((Transaction) pair.getValue());
        }

        transactionGroup.setTransaction(processedTransaction);
        transactionGroup.setTotalTransactions(totalTransactions);
        request.getStatement().setTransactionGroup(transactionGroup);
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
}
