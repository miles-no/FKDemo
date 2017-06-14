package no.fjordkraft.im.model;


import javax.persistence.*;
import java.util.List;

/**
 * Created by miles on 6/12/2017.
 */
@Entity
@Table(name="IM_LAYOUT_CONFIG")
public class LayoutConfig {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_LAYOUT_CONFIG_SEQ")
    private Long id;

    @Column(name="BRAND")
    private String brand;

    @Column(name="LEGAL_PART_CLASS")
    private String legalPartClass;

    @Column(name="ACCOUNT_CATEGORY")
    private String accountCategory;

    @Column(name="DISTRIBUTION_METHOD")
    private String distributionMethod;

    @Column(name="CREDIT_LIMIT")
    private boolean creditLimit;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "layoutConfig", fetch = FetchType.LAZY)
    private List<LayoutPayload> layoutPayload;

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

    public List<LayoutPayload> getLayoutPayload() {
        return layoutPayload;
    }

    public void setLayoutPayload(List<LayoutPayload> layoutPayload) {
        this.layoutPayload = layoutPayload;
    }

    /*@Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof LayoutConfig)) {
            return false;
        }

        LayoutConfig layoutConfig = (LayoutConfig) object;

        if(this.brand == layoutConfig.getBrand() &&
                this.legalPartClass == layoutConfig.getLegalPartClass() &&
                this.accountCategory == layoutConfig.getAccountCategory() &&
                this.distributionMethod == layoutConfig.getAccountCategory() &&
                this.creditLimit == layoutConfig.isCreditLimit() &&
                this.layoutPayload.equals(layoutConfig.getLayoutPayload())) {
            return true;
        }
        return false;
    }*/
}
