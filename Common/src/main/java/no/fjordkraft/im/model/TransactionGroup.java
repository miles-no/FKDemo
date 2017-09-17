package no.fjordkraft.im.model;

import javax.persistence.*;
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

    @Column(name="LABEL")
    private String label;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name="TG_ID")
    private List<TransactionGroupCategory> transactionGroupCategoryList;

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

    public List<TransactionGroupCategory> getTransactionGroupCategoryList() {
        return transactionGroupCategoryList;
    }

    public void setTransactionGroupCategoryList(List<TransactionGroupCategory> transactionGroupCategoryList) {
        this.transactionGroupCategoryList = transactionGroupCategoryList;
    }
}
