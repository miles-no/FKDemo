package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.domain.RestTransactionGroup;
import no.fjordkraft.im.model.TransactionGroup;
import no.fjordkraft.im.repository.TransactionGroupRepository;
import no.fjordkraft.im.services.TransactionGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by miles on 9/22/2017.
 */

@Service
public class TransactionGroupServiceImpl implements TransactionGroupService {

    @Autowired
    TransactionGroupRepository transactionGroupRepository;

    @Override
    public List<TransactionGroup> getTransactionGroupByBrand(String brand) {
        return transactionGroupRepository.queryTransactionGroupByBrand(brand);
    }

    @Override
    public List<String> queryCategoriesThroughBrand(String brand) {
        return transactionGroupRepository.queryCategoriesThroughBrand(brand);
    }

    @Override
    public List<RestTransactionGroup> getTransGrpCategory() {
        List<TransactionGroup> transactionGroupList = transactionGroupRepository.findAll();
        return null;
    }

}
