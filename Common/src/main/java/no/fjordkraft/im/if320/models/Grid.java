package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;

/**
 * Created by miles on 7/10/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "name",
        "email",
        "telephone"
})
@XmlRootElement(name = "Grid")
public class Grid {

    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "Email", required = true)
    protected String email;
    @XmlElement(name = "Telephone", required = true)
    protected String telephone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
