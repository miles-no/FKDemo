//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.02 at 07:13:02 PM IST 
//


package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element ref="{}FAKTURA"/&gt;
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
    "faktura","addedToList","displayStromData","attachmentNumber","startDate","endDate","isOnlyGrid"
})
@XmlRootElement(name = "Attachment")
public class Attachment {

    @XmlElement(name = "FAKTURA", required = true)
    protected FAKTURA faktura;

    @XmlElement(name = "AddedToList", required = true)
    protected Boolean addedToList;

    @XmlElement(name = "DisplayStromData", required = true)
    protected Boolean displayStromData;

    @XmlElement(name = "AttachmentNumber")
    protected int attachmentNumber;

    @XmlElement(name = "isOnlyGrid",required = false)
    protected boolean isOnlyGrid = false;

    @XmlElement(name = "StartDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar startDate;
    @XmlElement(name = "EndDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endDate;


    /**
     * Gets the value of the faktura property.
     * 
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.FAKTURA }
     *     
     */
    public FAKTURA getFAKTURA() {
        return faktura;
    }

    /**
     * Sets the value of the faktura property.
     * 
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.FAKTURA }
     *     
     */
    public void setFAKTURA(FAKTURA value) {
        this.faktura = value;
    }

    public Boolean getAddedToList() {
        return addedToList;
    }

    public void setAddedToList(Boolean addedToList) {
        this.addedToList = addedToList;
    }

    public Boolean getDisplayStromData() {
        return displayStromData;
    }

    public void setDisplayStromData(Boolean displayStromData) {
        this.displayStromData = displayStromData;
    }

    public FAKTURA getFaktura() {
        return faktura;
    }

    public void setFaktura(FAKTURA faktura) {
        this.faktura = faktura;
    }

    public int getAttachmentNumber() {
        return attachmentNumber;
    }

    public void setAttachmentNumber(int attachmentNumber) {
        this.attachmentNumber = attachmentNumber;
    }

    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    public void setStartDate(XMLGregorianCalendar startDate) {
        this.startDate = startDate;
    }

    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    public void setEndDate(XMLGregorianCalendar endDate) {
        this.endDate = endDate;
    }

    public boolean isOnlyGrid() {
        return isOnlyGrid;
    }

    public void setOnlyGrid(boolean onlyGrid) {
        isOnlyGrid = onlyGrid;
    }
}
