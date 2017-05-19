package no.fjordkraft.im.if320.models;

/**
 * Created by miles on 5/17/2017.
 */

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "consumptionList"
})
@XmlRootElement(name = "Consumptions")
public class Consumptions {
    @XmlElement(name = "Consumption", required = true)
    protected List<Consumption> consumptionList;

    public List<Consumption> getConsumptionList() {
        return consumptionList;
    }

    public void setConsumptionList(List<Consumption> consumptionList) {
        this.consumptionList = consumptionList;
    }
}
