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

    @Column(name="CREATE_TIME")
    private Timestamp createTime;

    @Column(name="UPDATED_TMS")
    private Timestamp updateTime;

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

    public Timestamp getcreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }


}
