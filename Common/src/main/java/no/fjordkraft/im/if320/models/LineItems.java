package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by bhavi on 12/12/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "lineItem"

})
@XmlRootElement(name = "Transactions")
public class LineItems {

    //@XmlElementRef(name = "LineItem", type = LineItem.class, required = false)
    //@XmlMixed
    @XmlElement(name = "LineItem")
    protected List<LineItem> lineItem;

    public List<LineItem> getLineItem() {
        return lineItem;
    }

    public void setLineItem(List<LineItem> lineItem) {
        this.lineItem = lineItem;
    }
}
