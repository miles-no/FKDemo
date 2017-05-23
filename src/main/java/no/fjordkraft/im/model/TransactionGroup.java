package no.fjordkraft.im.model;

import javax.persistence.*;

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

    @Column(name="LABEL")
    private String label;

    @Column(name="TRANSACTION_CATEGORY")
    private String transactionCategory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }
}
