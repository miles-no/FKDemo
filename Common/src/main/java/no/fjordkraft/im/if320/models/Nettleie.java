package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by miles on 9/8/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "referenceNumber",
        "baseItemDetails",
        "invoiceSummary"
})
@XmlRootElement(name = "Nettleie")
public class Nettleie {

    @XmlElement(name = "baseItemDetails")
    protected List<BaseItemDetails> baseItemDetails;
    @XmlElement(name = "ReferenceNumber")
    protected String referenceNumber;
    @XmlElement(name = "InvoiceSummary")
    protected InvoiceSummary invoiceSummary;

    public List<BaseItemDetails> getBaseItemDetails() {
        return baseItemDetails;
    }

    public void setBaseItemDetails(List<BaseItemDetails> baseItemDetails) {
        this.baseItemDetails = baseItemDetails;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public InvoiceSummary getInvoiceSummary() {
        return invoiceSummary;
    }

    public void setInvoiceSummary(InvoiceSummary invoiceSummary) {
        this.invoiceSummary = invoiceSummary;
    }
}
