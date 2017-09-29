package no.fjordkraft.im.domain;

import no.fjordkraft.im.model.TransactionCategory;

import java.util.List;

/**
 * Created by miles on 9/25/2017.
 */
public class RestTransactionGroup {

    private Long id;
    private String name;
    private String brand;
    private String type;
    private String description;
    private List<TransactionCategory> transactionCategories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<TransactionCategory> getTransactionCategories() {
        return transactionCategories;
    }

    public void setTransactionCategories(List<TransactionCategory> transactionCategories) {
        this.transactionCategories = transactionCategories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
