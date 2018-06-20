//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.02 at 07:13:02 PM IST 
//


package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
 *         &lt;element ref="{}InvoiceOrderInfo-110"/&gt;
 *         &lt;element ref="{}ReadingInfo-111" minOccurs="0"/&gt;
 *         &lt;element ref="{}LastReading-112" minOccurs="0"/&gt;
 *         &lt;element ref="{}InvoiceOrderAmounts-113"/&gt;
 *         &lt;element ref="{}VatSpecInvoiceOrder-114"/&gt;
 *         &lt;element ref="{}PriceInfo-116" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{}SupplyPointInfo-117"/&gt;
 *         &lt;element ref="{}ProductParameters-118"/&gt;
 *         &lt;element ref="{}InvoiceLine-120" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{}YearlyConsumption-123"/&gt;
 *         &lt;element ref="{}GenInfoInvoiceOrder-125" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{}ConsumptionPillars-132"/&gt;
 *         &lt;element ref="{}InvoiceOrderEndRecord-134"/&gt;
 *         &lt;element ref="{}Consumptions"/&gt;
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
    "invoiceOrderInfo110",
    "readingInfo111",
    "lastReading112",
    "invoiceOrderAmounts113",
    "vatSpecInvoiceOrder114",
    "priceInfo116",
    "supplyPointInfo117",
    "productParameters118",
    "invoiceLine120",
    "yearlyConsumption123",
    "genInfoInvoiceOrder125",
    "consumptionPillars132",
        "consumptions",
    "invoiceOrderEndRecord134",
    "nettleie",
    "nettleieList",
    "sequenceNumber",
        "transactionName",
        "invoiceNo"
})
@XmlRootElement(name = "InvoiceOrder")
public class InvoiceOrder {

    @XmlElement(name = "InvoiceOrderInfo-110", required = true)
    protected InvoiceOrderInfo110 invoiceOrderInfo110;
    @XmlElement(name = "ReadingInfo-111")
    protected ReadingInfo111 readingInfo111;
    @XmlElement(name = "LastReading-112")
    protected LastReading112 lastReading112;
    @XmlElement(name = "InvoiceOrderAmounts-113", required = true)
    protected InvoiceOrderAmounts113 invoiceOrderAmounts113;
    @XmlElement(name = "VatSpecInvoiceOrder-114", required = true)
    protected VatSpecInvoiceOrder114 vatSpecInvoiceOrder114;
    @XmlElement(name = "PriceInfo-116")
    protected List<PriceInfo116> priceInfo116;
    @XmlElement(name = "SupplyPointInfo-117", required = true)
    protected SupplyPointInfo117 supplyPointInfo117;
    @XmlElement(name = "ProductParameters-118", required = true)
    protected ProductParameters118 productParameters118;
    @XmlElement(name = "InvoiceLine-120")
    protected List<InvoiceLine120> invoiceLine120;
    @XmlElement(name = "YearlyConsumption-123", required = true)
    protected YearlyConsumption123 yearlyConsumption123;
    @XmlElement(name = "GenInfoInvoiceOrder-125")
    protected List<GenInfoInvoiceOrder125> genInfoInvoiceOrder125;
    @XmlElement(name = "ConsumptionPillars-132", required = true)
    protected ConsumptionPillars132 consumptionPillars132;
    @XmlElement(name = "InvoiceOrderEndRecord-134", required = true)
    protected InvoiceOrderEndRecord134 invoiceOrderEndRecord134;
    @XmlElement(name = "Consumptions", required = true)
    protected Consumptions consumptions;
    @XmlElement(name = "Nettleie", required = true)
    protected Nettleie nettleie;
    @XmlElement(name = "NettleieList")
    protected List<Nettleie> nettleieList;
    @XmlTransient
    protected Map mapOfVatSumOfGross;
    @XmlElement(name = "SequenceNumber")
    protected int sequenceNumber;
    @XmlElement(name = "TransactionName")
    protected String transactionName;
    @XmlElement(name = "InvoiceNo")
    protected long invoiceNo;



    /**
     * Gets the value of the invoiceOrderInfo110 property.
     * 
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.InvoiceOrderInfo110 }
     *
     */
    public InvoiceOrderInfo110 getInvoiceOrderInfo110() {
        return invoiceOrderInfo110;
    }

    /**
     * Sets the value of the invoiceOrderInfo110 property.
     *
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.InvoiceOrderInfo110 }
     *
     */
    public void setInvoiceOrderInfo110(InvoiceOrderInfo110 value) {
        this.invoiceOrderInfo110 = value;
    }

    /**
     * Gets the value of the readingInfo111 property.
     *
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.ReadingInfo111 }
     *
     */
    public ReadingInfo111 getReadingInfo111() {
        return readingInfo111;
    }

    /**
     * Sets the value of the readingInfo111 property.
     *
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.ReadingInfo111 }
     *
     */
    public void setReadingInfo111(ReadingInfo111 value) {
        this.readingInfo111 = value;
    }

    /**
     * Gets the value of the lastReading112 property.
     *
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.LastReading112 }
     *
     */
    public LastReading112 getLastReading112() {
        return lastReading112;
    }

    /**
     * Sets the value of the lastReading112 property.
     *
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.LastReading112 }
     *
     */
    public void setLastReading112(LastReading112 value) {
        this.lastReading112 = value;
    }

    /**
     * Gets the value of the invoiceOrderAmounts113 property.
     *
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.InvoiceOrderAmounts113 }
     *
     */
    public InvoiceOrderAmounts113 getInvoiceOrderAmounts113() {
        return invoiceOrderAmounts113;
    }

    /**
     * Sets the value of the invoiceOrderAmounts113 property.
     *
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.InvoiceOrderAmounts113 }
     *
     */
    public void setInvoiceOrderAmounts113(InvoiceOrderAmounts113 value) {
        this.invoiceOrderAmounts113 = value;
    }

    /**
     * Gets the value of the vatSpecInvoiceOrder114 property.
     *
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.VatSpecInvoiceOrder114 }
     *
     */
    public VatSpecInvoiceOrder114 getVatSpecInvoiceOrder114() {
        return vatSpecInvoiceOrder114;
    }

    /**
     * Sets the value of the vatSpecInvoiceOrder114 property.
     *
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.VatSpecInvoiceOrder114 }
     *
     */
    public void setVatSpecInvoiceOrder114(VatSpecInvoiceOrder114 value) {
        this.vatSpecInvoiceOrder114 = value;
    }

    /**
     * Gets the value of the priceInfo116 property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the priceInfo116 property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPriceInfo116().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link no.fjordkraft.im.if320.models.PriceInfo116 }
     *
     *
     */
    public List<PriceInfo116> getPriceInfo116() {
        if (priceInfo116 == null) {
            priceInfo116 = new ArrayList<PriceInfo116>();
        }
        return this.priceInfo116;
    }

    /**
     * Gets the value of the supplyPointInfo117 property.
     *
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.SupplyPointInfo117 }
     *
     */
    public SupplyPointInfo117 getSupplyPointInfo117() {
        return supplyPointInfo117;
    }

    /**
     * Sets the value of the supplyPointInfo117 property.
     *
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.SupplyPointInfo117 }
     *
     */
    public void setSupplyPointInfo117(SupplyPointInfo117 value) {
        this.supplyPointInfo117 = value;
    }

    /**
     * Gets the value of the productParameters118 property.
     *
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.ProductParameters118 }
     *
     */
    public ProductParameters118 getProductParameters118() {
        return productParameters118;
    }

    /**
     * Sets the value of the productParameters118 property.
     *
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.ProductParameters118 }
     *
     */
    public void setProductParameters118(ProductParameters118 value) {
        this.productParameters118 = value;
    }

    /**
     * Gets the value of the invoiceLine120 property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the invoiceLine120 property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInvoiceLine120().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link no.fjordkraft.im.if320.models.InvoiceLine120 }
     *
     *
     */
    public List<InvoiceLine120> getInvoiceLine120() {
        if (invoiceLine120 == null) {
            invoiceLine120 = new ArrayList<InvoiceLine120>();
        }
        return this.invoiceLine120;
    }

    /**
     * Gets the value of the yearlyConsumption123 property.
     *
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.YearlyConsumption123 }
     *
     */
    public YearlyConsumption123 getYearlyConsumption123() {
        return yearlyConsumption123;
    }

    /**
     * Sets the value of the yearlyConsumption123 property.
     *
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.YearlyConsumption123 }
     *
     */
    public void setYearlyConsumption123(YearlyConsumption123 value) {
        this.yearlyConsumption123 = value;
    }

    /**
     * Gets the value of the genInfoInvoiceOrder125 property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the genInfoInvoiceOrder125 property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGenInfoInvoiceOrder125().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link no.fjordkraft.im.if320.models.GenInfoInvoiceOrder125 }
     *
     *
     */
    public List<GenInfoInvoiceOrder125> getGenInfoInvoiceOrder125() {
        if (genInfoInvoiceOrder125 == null) {
            genInfoInvoiceOrder125 = new ArrayList<GenInfoInvoiceOrder125>();
        }
        return this.genInfoInvoiceOrder125;
    }

    /**
     * Gets the value of the consumptionPillars132 property.
     *
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.ConsumptionPillars132 }
     *
     */
    public ConsumptionPillars132 getConsumptionPillars132() {
        return consumptionPillars132;
    }

    /**
     * Sets the value of the consumptionPillars132 property.
     *
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.ConsumptionPillars132 }
     *
     */
    public void setConsumptionPillars132(ConsumptionPillars132 value) {
        this.consumptionPillars132 = value;
    }

    /**
     * Gets the value of the invoiceOrderEndRecord134 property.
     *
     * @return
     *     possible object is
     *     {@link no.fjordkraft.im.if320.models.InvoiceOrderEndRecord134 }
     *
     */
    public InvoiceOrderEndRecord134 getInvoiceOrderEndRecord134() {
        return invoiceOrderEndRecord134;
    }

    /**
     * Sets the value of the invoiceOrderEndRecord134 property.
     *
     * @param value
     *     allowed object is
     *     {@link no.fjordkraft.im.if320.models.InvoiceOrderEndRecord134 }
     *     
     */
    public void setInvoiceOrderEndRecord134(InvoiceOrderEndRecord134 value) {
        this.invoiceOrderEndRecord134 = value;
    }

    public Consumptions getConsumptions() {
        return consumptions;
    }

    public void setConsumptions(Consumptions consumptions) {
        this.consumptions = consumptions;
    }

    public Nettleie getNettleie() {
        return nettleie;
    }

    public void setNettleie(Nettleie nettleie) {
        this.nettleie = nettleie;
    }

    public List<Nettleie> getNettleieList() {
        if(null == nettleieList)
            nettleieList = new ArrayList<>();
        return nettleieList;
    }

    public void setNettleieList(List<Nettleie> nettleieList) {
        this.nettleieList = nettleieList;
    }

    public Map getMapOfVatSumOfGross() {
        return mapOfVatSumOfGross;
    }

    public void setMapOfVatSumOfGross(Map mapOfVatSumOfGross) {
        this.mapOfVatSumOfGross = mapOfVatSumOfGross;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public long getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(long invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
}
