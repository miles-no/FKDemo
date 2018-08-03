package no.fjordkraft.im.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by miles on 9/7/2017.
 */
@Table(name="IM_TRANSACTION_GRP_CATEGORY")
@Entity
public class TransactionGroupCategory {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="im_transactn_grp_category_seq")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="TG_ID")
    private TransactionGroup transactionGroup;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="TC_ID")
    private TransactionCategory transactionCategory;

    @Column(name="CREATED_TMS")
    private Timestamp createdTms;

    @Column(name="UPDATED_TMS")
    private Timestamp updatedTms;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionGroup getTransactionGroup() {
        return transactionGroup;
    }

    public void setTransactionGroup(TransactionGroup transactionGroup) {
        this.transactionGroup = transactionGroup;
    }

    public TransactionCategory getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(TransactionCategory transactionCategory) {
        this.transactionCategory = transactionCategory;
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
