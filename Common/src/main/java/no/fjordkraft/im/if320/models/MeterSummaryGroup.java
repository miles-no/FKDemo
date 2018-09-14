package no.fjordkraft.im.if320.models;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 9/4/18
 * Time: 11:24 AM
 * To change this template use File | Settings | File Templates.
 */

import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
      "meterSummary",
      "allMeterStromSum",
      "allMeterNettSum",
      "allMeterSum",
      "transactionSummary",
      "sumInclMva"
})
@XmlRootElement(name = "MeterSummaryGroup")
public class MeterSummaryGroup {

    @XmlElement(name="meterSummary")
    protected List<MeterSummary> meterSummary;

    @XmlElement(name="AllMeterStromSum")
    protected Double allMeterStromSum;

    @XmlElement(name="AllMeterNettSum")
    protected Double allMeterNettSum;

    @XmlElement(name="AllMeterSum")
    protected Double allMeterSum;

    @XmlElement(name="TransactionSummary")
    protected List<TransactionSummary> transactionSummary;

   @XmlElement(name="SumInclusivMva")
   protected Double sumInclMva;


    public List<MeterSummary> getMeterSummary() {
        return meterSummary;
    }

    public void setMeterSummary(List<MeterSummary> meterSummary) {
        this.meterSummary = meterSummary;
    }

    public Double getAllMeterNettSum() {
        return allMeterNettSum;
    }

    public void setAllMeterNettSum(Double allMeterNettSum) {
        this.allMeterNettSum = allMeterNettSum;
    }

    public Double getAllMeterStromSum() {
        return allMeterStromSum;
    }

    public void setAllMeterStromSum(Double allMeterStromSum) {
        this.allMeterStromSum = allMeterStromSum;
    }

    public Double getAllMeterSum() {
        return allMeterSum;
    }

    public void setAllMeterSum(Double allMeterSum) {
        this.allMeterSum = allMeterSum;
    }

    public List<TransactionSummary> getTransactionSummary() {
        return transactionSummary;
    }

    public void setTransactionSummary(List<TransactionSummary> transactionSummary) {
        this.transactionSummary = transactionSummary;
    }

    public Double getSumInclMva() {
        return sumInclMva;
    }

    public void setSumInclMva(Double sumInclMva) {
        this.sumInclMva = sumInclMva;
    }
}
