package no.fjordkraft.im.domain;

import java.io.InputStream;

/**
 * Created by miles on 6/13/2017.
 */
public class RestLayoutConfig {

    private Long id;
    private String brand;
    private String legalPartClass;
    private String accountCategory;
    private String distributionMethod;
    private boolean creditLimit;
    private InputStream inputStream;
    private int version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLegalPartClass() {
        return legalPartClass;
    }

    public void setLegalPartClass(String legalPartClass) {
        this.legalPartClass = legalPartClass;
    }

    public String getAccountCategory() {
        return accountCategory;
    }

    public void setAccountCategory(String accountCategory) {
        this.accountCategory = accountCategory;
    }

    public String getDistributionMethod() {
        return distributionMethod;
    }

    public void setDistributionMethod(String distributionMethod) {
        this.distributionMethod = distributionMethod;
    }

    public boolean isCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(boolean creditLimit) {
        this.creditLimit = creditLimit;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
