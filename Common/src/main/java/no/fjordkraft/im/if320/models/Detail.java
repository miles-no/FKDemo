package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 2/5/19
 * Time: 12:42 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "label",
        "expenditure"
})
@XmlRootElement(name = "Detail")
public class Detail {

    @XmlElement(name="Label")
    protected String label;

    @XmlElement(name="Expenditure")
    protected String expenditure;

    public String getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(String expenditure) {
        this.expenditure = expenditure;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
