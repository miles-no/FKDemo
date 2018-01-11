package no.fjordkraft.im.if320.models;

import com.sun.org.apache.xerces.internal.impl.dtd.XMLElementDecl;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by miles on 9/8/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "referenceNumber",
        "baseItemDetails",
        "invoiceSummary" ,
        "freeText",
        "description",
        "objectId",
        "meterId",
        "annualConsumption",
        "gridName",
        "isCreditNote"
     //   "invoice",
     //   "ehfInvoice"
})
@XmlRootElement(name = "Nettleie")
public class Nettleie {

    @XmlElement(name = "baseItemDetails")
    protected List<BaseItemDetails> baseItemDetails;
    @XmlElement(name = "ReferenceNumber")
    protected String referenceNumber;
    @XmlElement(name = "InvoiceSummary")
    protected InvoiceSummary invoiceSummary;
  /*  @XmlElement(name = "Invoice")
    protected Invoice invoice;

    @XmlElement(name = "ehfInvoice")
    protected oasis.names.specification.ubl.schema.xsd.invoice_2.Invoice ehfInvoice;*/
    @XmlElement(name = "freeText")
    protected String freeText;
    @XmlElement(name = "description")
    protected String description;
    @XmlElement(name = "objectId")
    protected long objectId;
    @XmlElement(name = "meterId")
    protected String meterId;
    @XmlElement(name="annualConsumption")
    protected long annualConsumption;
    @XmlElement(name = "gridName")
    protected String gridName;
    @XmlElement(name="isCreditNote")
    protected  boolean isCreditNote;


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

  /*  public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public oasis.names.specification.ubl.schema.xsd.invoice_2.Invoice getEhfInvoice() {
        return ehfInvoice;
    }

    public void setEhfInvoice(oasis.names.specification.ubl.schema.xsd.invoice_2.Invoice ehfInvoice) {
        this.ehfInvoice = ehfInvoice;
    }*/

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public long getAnnualConsumption() {
        return annualConsumption;
    }

    public void setAnnualConsumption(long annualConsumption) {
        this.annualConsumption = annualConsumption;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    public boolean isCreditNote() {
        return isCreditNote;
    }

    public void setCreditNote(boolean creditNote) {
        isCreditNote = creditNote;
    }
}
