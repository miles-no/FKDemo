package no.fjordkraft.im.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by miles on 5/18/2017.
 */
@Entity
@Table(name="IM_BRAND_CONFIG")
public class BrandConfig {

    public BrandConfig() {
    }

    public BrandConfig(String brand, char useEABarcode, String agreementNumber, String serviceLevel, String prefixKID, String kontonummer,
                       String description, String postcode, String city, String nationalId, String region) {
        this.brand = brand;
        this.useEABarcode = useEABarcode;
        this.agreementNumber = agreementNumber;
        this.serviceLevel = serviceLevel;
        this.prefixKID = prefixKID;
        this.kontonummer = kontonummer;
        this.description = description;
        this.postcode = postcode;
        this.city = city;
        this.nationalId = nationalId;
        this.region = region;
    }

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_BRAND_CONFIG_SEQ")
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

    @Column(name="KONTONUMMER")
    private String kontonummer;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="POSTCODE")
    private String postcode;

    @Column(name="CITY")
    private String city;

    @Column(name="NATIONALID")
    private String nationalId;

    @Column(name="REGION")
    private String region;

    @Column(name="CREATED_TMS")
    private Timestamp createdTms;

    @Column(name="UPDATED_TMS")
    private Timestamp updatedTms;

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

    public String getKontonummer() {
        return kontonummer;
    }

    public void setKontonummer(String kontonummer) {
        this.kontonummer = kontonummer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Timestamp getCreatedTms() {
        return createdTms;
    }

    public void setCreatedTms(Timestamp createdTms) {
        this.createdTms = createdTms;
    }

    public Timestamp getUpdatedTms() {
        return updatedTms;
    }

    public void setUpdatedTms(Timestamp updatedTms) {
        this.updatedTms = updatedTms;
    }
}
