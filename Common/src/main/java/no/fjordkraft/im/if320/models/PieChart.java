package no.fjordkraft.im.if320.models;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 2/5/19
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "detail"
}  )
@XmlRootElement(name = "PieChart")
public class PieChart {
    @XmlElement(name="Detail")
    protected List<Detail> detail;

    public List<Detail> getDetail() {
        return detail;
    }

    public void setDetail(List<Detail> detail) {
        this.detail = detail;
    }
}
