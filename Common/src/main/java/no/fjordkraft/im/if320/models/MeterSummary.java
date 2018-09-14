package no.fjordkraft.im.if320.models;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 9/3/18
 * Time: 2:22 PM
 * To change this template use File | Settings | File Templates.
 */

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "address",
        "renseanlegg",
        "målepunktID",
        "målernummer",
        "reference",
        "meterDetails",
        "sumOfNettStrom",
        "sumExclMva",
        "sequenceNumber",
        "meterNettStrom"
})

@XmlRootElement(name = "MeterSummary")
public class MeterSummary {

    @XmlElement(name = "Address")
    private String address;
    @XmlElement(name = "Renseanlegg")
    private String renseanlegg;
    @XmlElement(name = "MålepunktID")
    private Long målepunktID;
    @XmlElement(name = "Målernummer")
    private String målernummer;
    @XmlElement(name="Reference")
    private String reference;
    @XmlElement(name="MeterDetails")
    protected List<MeterDetails> meterDetails;
    @XmlElement(name="SumOfNettStrom")
    protected Double sumOfNettStrom;
    @XmlElement(name="SequenceNumber")
    protected int sequenceNumber;
    @XmlElement(name="MeterNettStrom")
    protected  Double meterNettStrom;
    @XmlElement(name="SumExclMva")
    protected Double sumExclMva;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<MeterDetails> getMeterDetails() {
        return meterDetails;
    }

    public void setMeterDetails(List<MeterDetails> meterDetails) {
        this.meterDetails = meterDetails;
    }

    public Long getMålepunktID() {
        return målepunktID;
    }

    public void setMålepunktID(Long målepunktID) {
        this.målepunktID = målepunktID;
    }

    public String getMålernummer() {
        return målernummer;
    }

    public void setMålernummer(String målernummer) {
        this.målernummer = målernummer;
    }

    public String getRenseanlegg() {
        return renseanlegg;
    }

    public void setRenseanlegg(String renseanlegg) {
        this.renseanlegg = renseanlegg;
    }

    public Double getSumExclMva() {
        return sumExclMva;
    }

    public void setSumExclMva(Double sumExclMva) {
        this.sumExclMva = sumExclMva;
    }

    public Double getSumOfNettStrom() {
        return sumOfNettStrom;
    }

    public void setSumOfNettStrom(Double sumOfNettStrom) {
        this.sumOfNettStrom = sumOfNettStrom;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Double getMeterNettStrom() {
        return meterNettStrom;
    }

    public void setMeterNettStrom(Double meterNettStrom) {
        this.meterNettStrom = meterNettStrom;
    }
}
