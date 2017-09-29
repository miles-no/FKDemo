package no.fjordkraft.im.services;

import no.fjordkraft.im.domain.RestTransactionGroup;
import no.fjordkraft.im.model.TransactionGroup;

import java.util.List;

/**
 * Created by miles on 9/22/2017.
 */
public interface TransactionGroupService {

    List<TransactionGroup> getTransactionGroupByBrand(String brand);
    List<String> queryCategoriesThroughBrand(String brand);
    List<RestTransactionGroup> getTransGrpCategory();
}
