package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.if320.models.Transaction;
import no.fjordkraft.im.if320.models.TransactionGroup;
import no.fjordkraft.im.model.TransactionGroupCategory;
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
public class CustomTransactionGroupPreprocessor extends BasePreprocessor {

    @Autowired
    TransactionGroupRepository transactionGroupRepository;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {
        Map<String, Transaction> diverseRabatter = new HashMap<String, Transaction>();

        List<Transaction> transactions = request.getStatement().getTransactions().getTransaction();
        Iterator mapIterator = null;
        int totalTransactions = request.getStatement().getTransactionGroup().getTotalTransactions();
        List<Transaction> processedTransaction = request.getStatement().getTransactionGroup().getTransaction();
        TransactionGroup transactionGroup = new TransactionGroup();

        Float amountWithVatTotal = 0.0f;
        Float vatTotalAmount = 0.0f;

        try {
            List<no.fjordkraft.im.model.TransactionGroup> transactionGroupList = transactionGroupRepository.findAll();

            for (Transaction transaction : transactions) {
                if (null != transactionGroupList && IMConstants.ZERO != transactionGroupList.size()) {
                    for (no.fjordkraft.im.model.TransactionGroup group : transactionGroupList) {
                        for (TransactionGroupCategory category : group.getTransactionGroupCategoryList()) {
                            if (category.getTransactionCategory().equals(transaction.getTransactionCategory())) {
                                diverseRabatter.put(group.getLabel(), createDiverseRabatterTransactionEntry(diverseRabatter, transaction, group, group.getName()));
                                amountWithVatTotal += transaction.getAmountWithVat();
                                vatTotalAmount += transaction.getVatAmount();
                            }
                        }
                    }
                }
            }

            Transaction transaction;
            mapIterator = diverseRabatter.entrySet().iterator();
            while (mapIterator.hasNext()) {
                Map.Entry pair = (Map.Entry) mapIterator.next();
                transaction = new Transaction();
                transaction = (Transaction) pair.getValue();
                transaction.setTransactionSequence(++totalTransactions);
                processedTransaction.add((Transaction) pair.getValue());
            }

            transactionGroup.setTransaction(processedTransaction);
            transactionGroup.setTotalTransactions(totalTransactions);
            request.getStatement().setTransactionGroup(transactionGroup);
            request.getStatement().getTransactions().setDiAmountWithVat(amountWithVatTotal);
            request.getStatement().getTransactions().setDiVatTotal(vatTotalAmount);
        } catch(Exception e) {
            throw new PreprocessorException(e.getMessage());
        }
    }

    private Transaction createDiverseRabatterTransactionEntry(Map<String, Transaction> diverseRabatter, Transaction transaction,
                                                              no.fjordkraft.im.model.TransactionGroup group, String groupName) {
        Transaction existingElement = new Transaction();
        Transaction resultTransaction = new Transaction();
        float newAmount;
        Iterator existingElementIterator;

        resultTransaction.setTransactionCategory(group.getLabel());
        if(diverseRabatter.isEmpty()) {
            resultTransaction.setAmountWithVat(transaction.getAmountWithVat());
        } else {
            existingElement = diverseRabatter.get(groupName);
            if(null == existingElement) {
                resultTransaction.setAmountWithVat(transaction.getAmountWithVat());
            } else {
                newAmount = existingElement.getAmountWithVat() + transaction.getAmountWithVat();
                resultTransaction.setAmountWithVat(newAmount);
            }
        }
        return resultTransaction;
    }

    public void setTransactionGroupRepository(TransactionGroupRepository transactionGroupRepository) {
        this.transactionGroupRepository = transactionGroupRepository;
    }
}
