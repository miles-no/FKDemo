package no.fjordkraft.im.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by miles on 9/22/2017.
 */
@Table(name="IM_TRANSACTION_CATEGORY")
@Entity
public class TransactionCategory {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_TRANSACTION_CATEGORY_SEQ")
    private Long id;

    @Column(name="TYPE")
    private String type;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="CATEGORY")
    private String category;

    @Column(name="CREATED_TMS")
    private Timestamp createdTms;

    @Column(name="LAST_UPDATED")
    private Timestamp updateTms;

    @JsonIgnore
    @OneToMany(mappedBy = "transactionCategory", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private List<TransactionGroupCategory> transGrpCategories;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<TransactionGroupCategory> getTransactionGroupCategories() {
        return transGrpCategories;
    }

    public void setTransactionGroupCategories(List<TransactionGroupCategory> transactionGroupCategories) {
        this.transGrpCategories = transactionGroupCategories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedTms() {
        return createdTms;
    }

    public void setCreatedTms(Timestamp createdTms) {
        this.createdTms = createdTms;
    }

    public Timestamp getUpdateTms() {
        return updateTms;
    }

    public void setUpdateTms(Timestamp updateTms) {
        this.updateTms = updateTms;
    }
}
