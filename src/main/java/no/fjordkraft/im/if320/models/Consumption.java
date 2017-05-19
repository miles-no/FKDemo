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
    protected float lastYearConsumption;
    @XmlElement(name = "ThisYearConsumption")
    protected float thisYearConsumption;

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

    public float getLastYearConsumption() {
        return lastYearConsumption;
    }

    public void setLastYearConsumption(float lastYearConsumption) {
        this.lastYearConsumption = lastYearConsumption;
    }

    public float getThisYearConsumption() {
        return thisYearConsumption;
    }

    public void setThisYearConsumption(float thisYearConsumption) {
        this.thisYearConsumption = thisYearConsumption;
    }
}
