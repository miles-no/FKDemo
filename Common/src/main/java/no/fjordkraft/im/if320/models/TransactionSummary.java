package no.fjordkraft.im.if320.models;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 5/16/18
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "transactionCategory",
        "transactionType",
        "sumOfBelop",
        "mvaValue" ,
        "sumOfNettStrom",
        "totalVatAmount"
})


@XmlRootElement(name = "TransactionSummary")
public class TransactionSummary {
    @XmlElement(name = "TransactionCategory")
    protected String transactionCategory;
    @XmlElement(name = "TransactionType")
    protected String transactionType;
    @XmlElement(name = "SumOfBelop")
    protected float sumOfBelop;
    @XmlElement(name = "MVAValue")
    protected float mvaValue;
    @XmlElement(name = "SumOfNettStrom")
    protected float sumOfNettStrom;
    @XmlElement(name = "TotalVatAmount")
    protected float totalVatAmount;


    public float getMvaValue() {
        return mvaValue;
    }

    public void setMvaValue(float mvaValue) {
        this.mvaValue = mvaValue;
    }

    public float getSumOfBelop() {
        return sumOfBelop;
    }

    public void setSumOfBelop(float sumOfBelop) {
        this.sumOfBelop = sumOfBelop;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public float getSumOfNettStrom() {
        return sumOfNettStrom;
    }

    public void setSumOfNettStrom(float sumOfNettStrom) {
        this.sumOfNettStrom = sumOfNettStrom;
    }

    public float getTotalVatAmount() {
        return totalVatAmount;
    }

    public void setTotalVatAmount(float totalVatAmount) {
        this.totalVatAmount = totalVatAmount;
    }
}
