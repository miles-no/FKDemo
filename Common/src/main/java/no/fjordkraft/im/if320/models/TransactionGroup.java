package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by miles on 5/19/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "transaction",
        "totalTransactions",
        "transactionSummary",
        "sumOfTransactions"
})
@XmlRootElement(name = "TransactionGroup")
public class TransactionGroup {

    @XmlElement(name = "Transaction")
    protected List<Transaction> transaction;

    @XmlElement(name = "TransactionSummary")
    protected List<TransactionSummary> transactionSummary;

    @XmlElement(name = "TotalTransactions")
    protected int totalTransactions;

    @XmlElement(name = "SumOfTransactions")
    protected float sumOfTransactions;

    public List<Transaction> getTransaction() {
        if(null == transaction) {
            transaction = new ArrayList<>();
        }
        return transaction;
    }

    public void setTransaction(List<Transaction> transaction) {
        this.transaction = transaction;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public List<TransactionSummary> getTransactionSummary() {
        return transactionSummary;
    }

    public void setTransactionSummary(List<TransactionSummary> transactionSummary) {
        this.transactionSummary = transactionSummary;
    }

    public float getSumOfTransactions() {
        return sumOfTransactions;
    }

    public void setSumOfTransactions(float sumOfTransactions) {
        this.sumOfTransactions = sumOfTransactions;
    }
}
