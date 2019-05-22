package no.fjordkraft.im.if320.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 2/5/19
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "sequence",
        "period",
        "deposit",
        "withdrawal"
}  )
public class BarGraphDetail {

    @XmlElement(name="Sequence")
    protected int sequence;

    @XmlElement(name="Period")
    protected String period;

    @XmlElement(name="Deposit")
    protected String deposit;

    @XmlElement(name="Withdrawal")
    protected String withdrawal;


    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(String withdrawal) {
        this.withdrawal = withdrawal;
    }
}
