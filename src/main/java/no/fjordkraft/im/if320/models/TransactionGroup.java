package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by miles on 5/19/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "transaction",
        "totalTransactions"
})
@XmlRootElement(name = "TransactionGroup")
public class TransactionGroup {

    @XmlElement(name = "Transaction")
    protected List<Transaction> transaction;

    @XmlElement(name = "TotalTransactions")
    protected int totalTransactions;

    public List<Transaction> getTransaction() {
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
}
