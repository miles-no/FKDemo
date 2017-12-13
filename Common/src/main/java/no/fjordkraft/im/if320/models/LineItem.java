package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Created by bhavi on 12/12/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "lineItemId",
        "closedClaimStatementSequenceNumber",
        "lineItemCategory",
        "lineItemDate",
        "amount",
        "vatAmount",
        "amountWithVat"
})
@XmlRootElement(name = "LineItem")
public class LineItem {

    @XmlElement(name = "LineItemId")
    protected long lineItemId;
    @XmlElement(name = "ClosedClaimStatementSequenceNumber")
    protected int closedClaimStatementSequenceNumber;
    @XmlElement(name = "LineItemCategory", required = true)
    protected String lineItemCategory;
    @XmlElement(name = "LineItemDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar lineItemDate;
    @XmlElement(name = "Amount")
    protected float amount;
    @XmlElement(name = "VatAmount")
    protected float vatAmount;
    @XmlElement(name = "AmountWithVat")
    protected float amountWithVat;

    public int getClosedClaimStatementSequenceNumber() {
        return closedClaimStatementSequenceNumber;
    }

    public void setClosedClaimStatementSequenceNumber(int closedClaimStatementSequenceNumber) {
        this.closedClaimStatementSequenceNumber = closedClaimStatementSequenceNumber;
    }

    public long getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(long lineItemId) {
        this.lineItemId = lineItemId;
    }

    public String getLineItemCategory() {
        return lineItemCategory;
    }

    public void setLineItemCategory(String lineItemCategory) {
        this.lineItemCategory = lineItemCategory;
    }

    public XMLGregorianCalendar getLineItemDate() {
        return lineItemDate;
    }

    public void setLineItemDate(XMLGregorianCalendar lineItemDate) {
        this.lineItemDate = lineItemDate;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(float vatAmount) {
        this.vatAmount = vatAmount;
    }

    public float getAmountWithVat() {
        return amountWithVat;
    }

    public void setAmountWithVat(float amountWithVat) {
        this.amountWithVat = amountWithVat;
    }
}

