package no.fjordkraft.im.model;

import javax.persistence.*;

/**
 * Created by miles on 5/18/2017.
 */
@Entity
@Table(name="IM_BARCODE_CONFIG")
public class BarcodeConfig {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_BARCODE_CONFIG_SEQ")
    private Long id;

    @Column(name="BRAND")
    private String brand;

    @Column(name="USE_EA_BARCODE")
    private char useEABarcode;

    @Column(name="AGREEMENT_NUMBER")
    private String agreementNumber;

    @Column(name="SERVICELEVEL")
    private String serviceLevel;

    @Column(name="PREFIX_KID")
    private String prefixKID;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public char getUseEABarcode() {
        return useEABarcode;
    }

    public void setUseEABarcode(char useEABarcode) {
        this.useEABarcode = useEABarcode;
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public String getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(String serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public String getPrefixKID() {
        return prefixKID;
    }

    public void setPrefixKID(String prefixKID) {
        this.prefixKID = prefixKID;
    }
}
