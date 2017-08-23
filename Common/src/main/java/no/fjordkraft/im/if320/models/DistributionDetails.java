package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;

/**
 * Created by miles on 5/23/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "type",
        "amount"
})
@XmlRootElement(name = "DistributionDetails")
public class DistributionDetails {

    @XmlElement(name = "Type", required = true)
    protected String type;
    @XmlElement(name = "Amount")
    protected float amount;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
