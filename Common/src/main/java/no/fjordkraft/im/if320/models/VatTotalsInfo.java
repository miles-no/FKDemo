package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 10/12/18
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "vatPercent",
        "vatBaseAmount",
        "vatAmount"
})
@XmlRootElement(name = "VatTotalsInfo")
public class VatTotalsInfo {

    @XmlElement(name = "VatPercent", required = true)
    protected double vatPercent;

    @XmlElement(name = "VatBaseAmount", required = true)
    protected double vatBaseAmount;

    @XmlElement(name = "VatAmount", required = true)
    protected double vatAmount;

    public double getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(double vatAmount) {
        this.vatAmount = vatAmount;
    }

    public double getVatBaseAmount() {
        return vatBaseAmount;
    }

    public void setVatBaseAmount(double vatBaseAmount) {
        this.vatBaseAmount = vatBaseAmount;
    }

    public double getVatPercent() {
        return vatPercent;
    }

    public void setVatPercent(double vatPercent) {
        this.vatPercent = vatPercent;
    }
}
