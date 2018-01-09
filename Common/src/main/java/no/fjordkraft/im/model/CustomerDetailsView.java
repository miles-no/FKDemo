package no.fjordkraft.im.model;


import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/8/18
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "v_customer_details")
public class CustomerDetailsView {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_cust_detail_SEQ")
    @Column(name="ACCOUNTNO", updatable = false, nullable = false)
    private String accountNumber;

    @Column(name="GIRO_ENABLED")
    private Boolean isGiroEnabled;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Boolean getGiroEnabled() {
        return isGiroEnabled;
    }

    public void setGiroEnabled(Boolean giroEnabled) {
        isGiroEnabled = giroEnabled;
    }
}
