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
    protected double sumOfBelop;
    @XmlElement(name = "MVAValue")
    protected double mvaValue;
    @XmlElement(name = "SumOfNettStrom")
    protected double sumOfNettStrom;
    @XmlElement(name = "TotalVatAmount")
    protected double totalVatAmount;


    public double getMvaValue() {
        return mvaValue;
    }

    public void setMvaValue(double mvaValue) {
        this.mvaValue = mvaValue;
    }

    public double getSumOfBelop() {
        return sumOfBelop;
    }

    public void setSumOfBelop(double sumOfBelop) {
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

    public double getSumOfNettStrom() {
        return sumOfNettStrom;
    }

    public void setSumOfNettStrom(double sumOfNettStrom) {
        this.sumOfNettStrom = sumOfNettStrom;
    }

    public double getTotalVatAmount() {
        return totalVatAmount;
    }

    public void setTotalVatAmount(double totalVatAmount) {
        this.totalVatAmount = totalVatAmount;
    }
}
