package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by miles on 9/5/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "description",
        "postcode",
        "city",
        "nationalId",
        "region"
})
public class ToAddress {

    @XmlElement(name = "Description", required = true)
    protected String description;
    @XmlElement(name = "PostCode", required = true)
    protected String postcode;
    @XmlElement(name = "City", required = true)
    protected String city;
    @XmlElement(name = "NationalId", required = true)
    protected String nationalId;
    @XmlElement(name = "Region", required = true)
    protected String region;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
