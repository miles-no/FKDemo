package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 9/3/18
 * Time: 2:35 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "meterName",
        "forbruk",
        "startDate",
        "endDate",
        "beløp"
})

@XmlRootElement(name = "MeterDetails")
public class MeterDetails {

    @XmlElement(name = "MeterName")
    private String meterName;

    @XmlElement(name = "Forbruk")
    private Double forbruk;
    @XmlElement(name = "StartDate")
    private XMLGregorianCalendar startDate;
    @XmlElement(name="EndDate")
    private XMLGregorianCalendar endDate;
    @XmlElement(name = "Beløp")
    private Double beløp;

    public Double getBeløp() {
        return beløp;
    }

    public void setBeløp(Double beløp) {
        this.beløp = beløp;
    }

    public Double getForbruk() {
        return forbruk;
    }

    public void setForbruk(Double forbruk) {
        this.forbruk = forbruk;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    public void setEndDate(XMLGregorianCalendar endDate) {
        this.endDate = endDate;
    }

    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    public void setStartDate(XMLGregorianCalendar startDate) {
        this.startDate = startDate;
    }
}
