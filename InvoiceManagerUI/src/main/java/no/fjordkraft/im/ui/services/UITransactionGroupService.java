package no.fjordkraft.im.ui.services;

import no.fjordkraft.im.domain.RestTransactionCategory;
import no.fjordkraft.im.domain.RestTransactionGroup;
import no.fjordkraft.im.model.TransactionCategory;

import java.util.List;

/**
 * Created by miles on 9/26/2017.
 */
public interface UITransactionGroupService {

    List<RestTransactionGroup> getAll();
    void saveTransactionGroup(RestTransactionGroup restTransactionGroup);
    void updateTransactionGroup(Long id, RestTransactionGroup restTransactionGroup);
    void deleteTransactionGroup(Long id);
    List<RestTransactionGroup> getTransactionGroupByBrand(String brand);
    List<TransactionCategory> getTransactionCategoryOnType(String type);
    List<TransactionCategory> getAllTransactionCategories();

    //void  createTransactionCategory(RestTransactionCategory transactionCategory);
}
