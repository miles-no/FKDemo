package no.fjordkraft.im.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by miles on 5/23/2017.
 */
@Table(name="IM_TRANSACTION_GROUP")
@Entity
public class TransactionGroup {
    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_TRANSACTION_GROUP_SEQ")
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name="BRAND")
    private String brand;

    @Column(name="TYPE")
    private String type;

    @Column(name="DESCRIPTION")
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "transactionGroup",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TransactionGroupCategory> transactionGroupCategories;

    @Column(name="CREATED_TMS")
    private Timestamp createdTms;

    @Column(name="UPDATE_TMS")
    private Timestamp updateTms;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<TransactionGroupCategory> getTransactionGroupCategories() {
        return transactionGroupCategories;
    }

    public void setTransactionGroupCategories(List<TransactionGroupCategory> transactionGroupCategories) {
        this.transactionGroupCategories = transactionGroupCategories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
