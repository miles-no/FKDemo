package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;

/**
 * Created by miles on 5/17/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "consumptionSequence",
        "periodDescription",
        "lastYearConsumption",
        "thisYearConsumption"
})
@XmlRootElement(name = "Consumption")
public class Consumption {
    @XmlElement(name = "ConsumptionSequence", required = true)
    protected int consumptionSequence;
    @XmlElement(name = "PeriodDescription", required = true)
    protected String periodDescription;
    @XmlElement(name = "LastYearConsumption")
    protected double lastYearConsumption;
    @XmlElement(name = "ThisYearConsumption")
    protected double thisYearConsumption;

    public int getConsumptionSequence() {
        return consumptionSequence;
    }

    public void setConsumptionSequence(int consumptionSequence) {
        this.consumptionSequence = consumptionSequence;
    }

    public String getPeriodDescription() {
        return periodDescription;
    }

    public void setPeriodDescription(String periodDescription) {
        this.periodDescription = periodDescription;
    }

    public double getLastYearConsumption() {
        return lastYearConsumption;
    }

    public void setLastYearConsumption(double lastYearConsumption) {
        this.lastYearConsumption = lastYearConsumption;
    }

    public double getThisYearConsumption() {
        return thisYearConsumption;
    }

    public void setThisYearConsumption(double thisYearConsumption) {
        this.thisYearConsumption = thisYearConsumption;
    }
}
