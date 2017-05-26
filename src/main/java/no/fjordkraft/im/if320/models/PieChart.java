package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by miles on 5/24/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "distribution"
})
@XmlRootElement(name = "PieChart")
public class PieChart {
    @XmlElement(name = "DistributionDetails", required = true)
    protected List<DistributionDetails> distribution;

    public List<DistributionDetails> getDistribution() {
        return distribution;
    }

    public void setDistribution(List<DistributionDetails> distribution) {
        this.distribution = distribution;
    }
}
