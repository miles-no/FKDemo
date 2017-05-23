package no.fjordkraft.im.preprocess.services.impl;

/**
 * Created by miles on 5/22/2017.
 */
public class NettTransaction {
    private String transactionCategory;
    private String freeText;

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public int hashCode(){
        int hashcode = 0;
        hashcode = transactionCategory.hashCode();
        hashcode += freeText.hashCode();
        return hashcode;
    }

    public boolean equals(Object obj){
        if (obj instanceof NettTransaction) {
            NettTransaction nettTransaction = (NettTransaction) obj;
            return (nettTransaction.transactionCategory.equals(this.transactionCategory)
                    && nettTransaction.freeText.equals(this.freeText));
        } else {
            return false;
        }
    }
}
