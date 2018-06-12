package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;

/**
 * Created by bhavi on 6/12/2018.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "dummyId",

})
@XmlRootElement(name = "Dummy")
public class Dummy {
    @XmlElement(name = "DummyId")
    public String dummyId;

    public String getDummyId() {
        return dummyId;
    }

    public void setDummyId(String dummyId) {
        this.dummyId = dummyId;
    }
}
