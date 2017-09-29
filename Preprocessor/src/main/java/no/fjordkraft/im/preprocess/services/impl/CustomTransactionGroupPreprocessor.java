package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.if320.models.Transaction;
import no.fjordkraft.im.if320.models.TransactionGroup;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.services.TransactionGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by miles on 5/23/2017.
 */
@Service
@PreprocessorInfo(order=7)
public class CustomTransactionGroupPreprocessor extends BasePreprocessor {

    @Autowired
    TransactionGroupService transactionGroupService;

    @Autowired
    ConfigService configService;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {
        Map<String, Transaction> diverseRabatter = new HashMap<String, Transaction>();

        List<Transaction> transactions = request.getStatement().getTransactions().getTransaction();
        Iterator mapIterator = null;
        int totalTransactions = request.getStatement().getTransactionGroup().getTotalTransactions();
        List<Transaction> processedTransaction = request.getStatement().getTransactionGroup().getTransaction();
        TransactionGroup transactionGroup = new TransactionGroup();
        String brand = request.getEntity().getSystemBatchInput().getTransferFile().getBrand();

        Float amountWithVatTotal = 0.0f;
        Float vatTotalAmount = 0.0f;

        try {
            Map<String, Set<String>> transactionGroupMap = configService.getTransactionGroupForBrand(brand);

            if(null != transactionGroupMap) {
                Set<Map.Entry<String, Set<String>>> entrySet = transactionGroupMap.entrySet();
                for (Transaction transaction : transactions) {
                    for (Map.Entry<String, Set<String>> entry : entrySet) {
                        if (entry.getValue().contains(transaction.getTransactionCategory())) {
                            diverseRabatter.put(entry.getKey(), createDiverseRabatterTransactionEntry(diverseRabatter, transaction, entry.getKey()));
                            amountWithVatTotal += transaction.getAmountWithVat();
                            vatTotalAmount += transaction.getVatAmount();
                            break;
                        }
                    }
                }

                Transaction transaction;
                for (Map.Entry<String, Transaction> entry : diverseRabatter.entrySet()) {
                    transaction = entry.getValue();
                    transaction.setTransactionSequence(++totalTransactions);
                    processedTransaction.add(entry.getValue());
                }

                transactionGroup.setTransaction(processedTransaction);
                transactionGroup.setTotalTransactions(totalTransactions);
                request.getStatement().setTransactionGroup(transactionGroup);
                request.getStatement().getTransactions().setDiAmountWithVat(amountWithVatTotal);
                request.getStatement().getTransactions().setDiVatTotal(vatTotalAmount);
            }
        } catch(Exception e) {
            throw new PreprocessorException(e.getMessage());
        }
    }

    private Transaction createDiverseRabatterTransactionEntry(Map<String, Transaction> diverseRabatter, Transaction transaction,
                                                              String groupName) {
        Transaction existingElement = new Transaction();
        Transaction resultTransaction = new Transaction();
        float newAmount;

        resultTransaction.setTransactionCategory(groupName);
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

    public void setTransactionGroupService(TransactionGroupService transactionGroupService) {
        this.transactionGroupService = transactionGroupService;
    }
}
