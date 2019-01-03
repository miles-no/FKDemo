package no.fjordkraft.im.model;

import javax.lang.model.element.Name;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 10/5/18
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name= "AFI_ACCOUNTDETAILS"/*,schema = "eacprod"*/)
public class AFIAccountDetails {

   @Id
   @Column(name = "ACCOUNTNO")
   private String accountNo;
    @Column(name = "BIC")
   private String bic;
   @Column(name = "IBAN")
   private String iban;
   @Column(name="TIME")
   private Timestamp time;
   @Column(name="PRINTDISTRIBUTOR")
   private String printdistributor;
   @Column(name = "EHFTRD")
   private int ehftrd;

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public int getEhftrd() {
        return ehftrd;
    }

    public void setEhftrd(int ehftrd) {
        this.ehftrd = ehftrd;
    }

    public Timestamp getHjemmeladingCreated() {
        return hjemmeladingCreated;
    }

    public void setHjemmeladingCreated(Timestamp hjemmeladingCreated) {
        this.hjemmeladingCreated = hjemmeladingCreated;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getInvoiceLayout() {
        return invoiceLayout;
    }

    public void setInvoiceLayout(String invoiceLayout) {
        this.invoiceLayout = invoiceLayout;
    }

    public int getInvoiceManager() {
        return invoiceManager;
    }

    public void setInvoiceManager(int invoiceManager) {
        this.invoiceManager = invoiceManager;
    }

    public String getPrintdistributor() {
        return printdistributor;
    }

    public void setPrintdistributor(String printdistributor) {
        this.printdistributor = printdistributor;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Column(name="INVOICEMANAGER")

   private int invoiceManager;
   @Column(name="HJEMMELADING_CREATED")
   private Timestamp hjemmeladingCreated;
   @Column(name="INVOICE_LAYOUT")
   private String invoiceLayout;



}
