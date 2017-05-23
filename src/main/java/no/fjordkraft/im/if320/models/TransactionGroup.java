package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by miles on 5/19/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "transaction"
})
@XmlRootElement(name = "TransactionGroup")
public class TransactionGroup {

    @XmlElement(name = "Transaction")
    protected List<Transaction> transaction;

    public List<Transaction> getTransaction() {
        return transaction;
    }

    public void setTransaction(List<Transaction> transaction) {
        this.transaction = transaction;
    }
}
