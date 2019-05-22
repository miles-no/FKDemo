package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 2/5/19
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "barGraphDetail"
}  )
@XmlRootElement(name = "BarGraph")
public class BarGraph {

    @XmlElement(name="BarGraphDetail")
    protected List<BarGraphDetail> barGraphDetail;

    public List<BarGraphDetail> getBarGraphDetail() {
        return barGraphDetail;
    }

    public void setBarGraphDetail(List<BarGraphDetail> barGraphDetail) {
        this.barGraphDetail = barGraphDetail;
    }
}
