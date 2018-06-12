package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by bhavi on 6/12/2018.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "dummy"
})
@XmlRootElement(name = "Dummy")
public class Dummies {

    @XmlElement(name = "Dummy")
    protected List<Dummy> dummy;

    public List<Dummy> getDummy() {
        return dummy;
    }

    public void setDummy(List<Dummy> dummy) {
        this.dummy = dummy;
    }
}
