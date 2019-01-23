//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.12 at 05:23:10 PM IST 
//


package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}Level"/&gt;
 *         &lt;element ref="{}SuppliersProductId"/&gt;
 *         &lt;element ref="{}Description"/&gt;
 *         &lt;element ref="{}UnitPrice"/&gt;
 *         &lt;element ref="{}PriceDenomination"/&gt;
 *         &lt;element ref="{}LineItemAmount"/&gt;
 *         &lt;element ref="{}LineItemGrossAmount"/&gt;
 *         &lt;element ref="{}QuantityInvoiced"/&gt;
 *         &lt;element ref="{}UnitOfMeasure"/&gt;
 *         &lt;element ref="{}VatInfo"/&gt;
 *         &lt;element ref="{}StartDate"/&gt;
 *         &lt;element ref="{}EndDate"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "level",
    "suppliersProductId",
    "description",
    "unitPrice",
    "priceDenomination",
    "lineItemAmount",
    "lineItemGrossAmount",
    "quantityInvoiced",
    "unitOfMeasure",
    "vatInfo",
    "startDate",
    "endDate",
    "unitPriceGross",
    "ref",
    "noOfDays",
    "attachmentFormat",
    "vatRate",
    "lineExtensionAmount",
    "baseQuantity"
})
@XmlRootElement(name = "BaseItemDetails")
public class BaseItemDetails {

    @XmlElement(name = "Level")
    protected byte level;
    @XmlElement(name = "SuppliersProductId")
    protected byte suppliersProductId;
    @XmlElement(name = "Description", required = true)
    protected String description;
    @XmlElement(name = "UnitPrice")
    protected double unitPrice;
    @XmlElement(name = "PriceDenomination", required = true)
    protected String priceDenomination;
    @XmlElement(name = "LineItemAmount")
    protected double lineItemAmount;
    @XmlElement(name = "LineItemGrossAmount")
    protected double lineItemGrossAmount;
    @XmlElement(name = "QuantityInvoiced")
    protected double quantityInvoiced;
    @XmlElement(name = "UnitOfMeasure", required = true)
    protected String unitOfMeasure;
    @XmlElement(name = "VatInfo", required = true)
    protected VatInfo vatInfo;
    @XmlElement(name = "StartDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar startDate;
    @XmlElement(name = "EndDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endDate;

    @XmlElement(name = "UnitPriceGross")
    protected double unitPriceGross;

    @XmlElement(name = "Ref")
    protected List<Ref> ref;

    @XmlElement(name = "NoOfDays")
    protected long noOfDays;

    @XmlElement(name = "AttachmentFormat")
    protected String attachmentFormat;

    @XmlElement(name = "VatRate")
    protected double vatRate;
    @XmlElement(name = "LineExtensionAmount")
    protected double lineExtensionAmount;

    @XmlElement(name="BaseQuantity")
    protected int baseQuantity;

    /**
     * Gets the value of the level property.
     * 
     */
    public byte getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     */
    public void setLevel(byte value) {
        this.level = value;
    }

    /**
     * Gets the value of the suppliersProductId property.
     * 
     */
    public byte getSuppliersProductId() {
        return suppliersProductId;
    }

    /**
     * Sets the value of the suppliersProductId property.
     * 
     */
    public void setSuppliersProductId(byte value) {
        this.suppliersProductId = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the unitPrice property.
     * 
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the value of the unitPrice property.
     * 
     */
    public void setUnitPrice(double value) {
        this.unitPrice = value;
    }

    /**
     * Gets the value of the priceDenomination property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriceDenomination() {
        return priceDenomination;
    }

    /**
     * Sets the value of the priceDenomination property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriceDenomination(String value) {
        this.priceDenomination = value;
    }

    /**
     * Gets the value of the lineItemAmount property.
     * 
     */
    public double getLineItemAmount() {
        return lineItemAmount;
    }

    /**
     * Sets the value of the lineItemAmount property.
     * 
     */
    public void setLineItemAmount(double value) {
        this.lineItemAmount = value;
    }

    /**
     * Gets the value of the lineItemGrossAmount property.
     * 
     */
    public double getLineItemGrossAmount() {
        return lineItemGrossAmount;
    }

    /**
     * Sets the value of the lineItemGrossAmount property.
     * 
     */
    public void setLineItemGrossAmount(double value) {
        this.lineItemGrossAmount = value;
    }

    /**
     * Gets the value of the quantityInvoiced property.
     * 
     */
    public double getQuantityInvoiced() {
        return quantityInvoiced;
    }

    /**
     * Sets the value of the quantityInvoiced property.
     * 
     */
    public void setQuantityInvoiced(double value) {
        this.quantityInvoiced = value;
    }

    /**
     * Gets the value of the unitOfMeasure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    /**
     * Sets the value of the unitOfMeasure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitOfMeasure(String value) {
        this.unitOfMeasure = value;
    }

    /**
     * Gets the value of the vatInfo property.
     * 
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.VatInfo }
     *     
     */
    public VatInfo getVatInfo() {
        return vatInfo;
    }

    /**
     * Sets the value of the vatInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.VatInfo }
     *     
     */
    public void setVatInfo(VatInfo value) {
        this.vatInfo = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setStartDate(XMLGregorianCalendar value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the endDate property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
    }

    public double getUnitPriceGross() {
        return unitPriceGross;
    }

    public void setUnitPriceGross(double unitPriceGross) {
        this.unitPriceGross = unitPriceGross;
    }

    public List<Ref> getRef() {
        return ref;
    }

    public void setRef(List<Ref> ref) {
        this.ref = ref;
    }

    public long getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(long noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getAttachmentFormat() {
        return attachmentFormat;
    }

    public void setAttachmentFormat(String attachmentFormat) {
        this.attachmentFormat = attachmentFormat;
    }

    public double getVatRate() {
        return vatRate;
    }

    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }

    public double getLineExtensionAmount() {
        return lineExtensionAmount;
    }

    public void setLineExtensionAmount(double lineExtensionAmount) {
        this.lineExtensionAmount = lineExtensionAmount;
    }

    public int getBaseQuantity() {
        return baseQuantity;
    }

    public void setBaseQuantity(int baseQuantity) {
        this.baseQuantity = baseQuantity;
    }
}
