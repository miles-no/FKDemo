package no.fjordkraft.im.model;

import javax.persistence.*;

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

    @Column(name="TG_ID")
    private Long transactionGroupId;

    @Column(name="TRANSACTION_CATEGORY")
    private String transactionCategory;

    public Long getTransactionGroupId() {
        return transactionGroupId;
    }

    public void setTransactionGroupId(Long transactionGroupId) {
        this.transactionGroupId = transactionGroupId;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }
}
