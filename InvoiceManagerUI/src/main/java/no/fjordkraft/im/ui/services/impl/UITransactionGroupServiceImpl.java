package no.fjordkraft.im.ui.services.impl;

import no.fjordkraft.im.domain.RestTransactionGroup;
import no.fjordkraft.im.model.TransactionCategory;
import no.fjordkraft.im.model.TransactionGroup;
import no.fjordkraft.im.model.TransactionGroupCategory;
import no.fjordkraft.im.repository.TransGrpCategoryRepository;
import no.fjordkraft.im.repository.TransactionCategoryRepository;
import no.fjordkraft.im.repository.TransactionGroupRepository;
import no.fjordkraft.im.ui.services.UITransactionGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by miles on 9/26/2017.
 */
@Service
public class UITransactionGroupServiceImpl implements UITransactionGroupService {

    @Autowired
    TransactionGroupRepository transactionGroupRepository;

    @Autowired
    TransactionCategoryRepository transactionCategoryRepository;

    @Autowired
    TransGrpCategoryRepository transGrpCategoryRepository;

    @Override
    public List<RestTransactionGroup> getAll() {
        List<TransactionGroup> transactionGroupList = transactionGroupRepository.findAll();
        List<RestTransactionGroup> restTransactionGroupList = new ArrayList<>();
        List<TransactionCategory> transactionCategoryList;
        TransactionCategory transactionCategory;
        RestTransactionGroup restTransactionGroup;

        for(TransactionGroup transactionGroup:transactionGroupList) {
            restTransactionGroup = new RestTransactionGroup();
            restTransactionGroup.setId(transactionGroup.getId());
            restTransactionGroup.setName(transactionGroup.getName());
            restTransactionGroup.setBrand(transactionGroup.getBrand());
            restTransactionGroup.setType(transactionGroup.getType());
            restTransactionGroup.setDescription(transactionGroup.getDescription());

            transactionCategoryList = new ArrayList<>();
            for(TransactionGroupCategory transactionGroupCategory:transactionGroup.getTransactionGroupCategories()) {
                transactionCategory = transactionGroupCategory.getTransactionCategory();
                transactionCategoryList.add(transactionCategory);
            }
            restTransactionGroup.setTransactionCategories(transactionCategoryList);
            restTransactionGroupList.add(restTransactionGroup);
        }

        return restTransactionGroupList;
    }

    @Override
    public List<RestTransactionGroup> getTransactionGroupByBrand(String brand) {
        List<TransactionGroup> transactionGroupList = transactionGroupRepository.queryTransactionGroupByBrand(brand);
        List<RestTransactionGroup> restTransactionGroupList = new ArrayList<>();
        List<TransactionCategory> transactionCategoryList;
        TransactionCategory transactionCategory;
        RestTransactionGroup restTransactionGroup;

        for(TransactionGroup transactionGroup:transactionGroupList) {
            restTransactionGroup = new RestTransactionGroup();
            restTransactionGroup.setName(transactionGroup.getName());
            restTransactionGroup.setBrand(transactionGroup.getBrand());
            restTransactionGroup.setType(transactionGroup.getType());
            restTransactionGroup.setDescription(transactionGroup.getDescription());

            transactionCategoryList = new ArrayList<>();
            for(TransactionGroupCategory transactionGroupCategory:transactionGroup.getTransactionGroupCategories()) {
                transactionCategory = transactionGroupCategory.getTransactionCategory();
                transactionCategoryList.add(transactionCategory);
            }
            restTransactionGroup.setTransactionCategories(transactionCategoryList);
            restTransactionGroupList.add(restTransactionGroup);
        }

        return restTransactionGroupList;
    }

    @Override
    public List<TransactionCategory> getTransactionCategoryOnType(String type) {
        return transactionCategoryRepository.getTransactionCategoryByType(type);
    }

    @Override
    public List<TransactionCategory> getAllTransactionCategories() {
        return transactionCategoryRepository.findAll();
    }

    @Override
    public void saveTransactionGroup(RestTransactionGroup restTransactionGroup) {

        TransactionGroupCategory transactionGroupCategory;
        List<TransactionGroupCategory> transactionGroupCategoryList = new ArrayList<>();

        TransactionGroup transactionGroup = new TransactionGroup();
        transactionGroup.setName(restTransactionGroup.getName());
        transactionGroup.setBrand(restTransactionGroup.getBrand());
        transactionGroup.setType(restTransactionGroup.getType());
        transactionGroup.setDescription(restTransactionGroup.getDescription());

        for(TransactionCategory transactionCategory:restTransactionGroup.getTransactionCategories()) {
            transactionGroupCategory = new TransactionGroupCategory();
            transactionGroupCategory.setTransactionGroup(transactionGroup);
            transactionGroupCategory.setTransactionCategory(transactionCategory);
            transactionGroupCategoryList.add(transactionGroupCategory);
        }
        transactionGroup.setTransactionGroupCategories(transactionGroupCategoryList);
        transactionGroupRepository.saveAndFlush(transactionGroup);
    }

    @Override
    @Transactional
    public void updateTransactionGroup(Long id, RestTransactionGroup restTransactionGroup) {
        TransactionGroupCategory transactionGroupCategory;
        List<TransactionGroupCategory> transactionGroupCategoryList = new ArrayList<>();

        TransactionGroup transactionGroup = transactionGroupRepository.findOne(id);
        transactionGroup.setName(restTransactionGroup.getName());
        transactionGroup.setBrand(restTransactionGroup.getBrand());
        transactionGroup.setType(restTransactionGroup.getType());
        transactionGroup.setDescription(restTransactionGroup.getDescription());

        for(TransactionGroupCategory transGrpCategory:transactionGroup.getTransactionGroupCategories()) {
            deleteTransGrpCategory(transGrpCategory.getId());
        }
        transactionGroup.getTransactionGroupCategories().clear();

        for(TransactionCategory transactionCategory:restTransactionGroup.getTransactionCategories()) {
            transactionGroupCategory = new TransactionGroupCategory();
            transactionGroupCategory.setTransactionGroup(transactionGroup);
            transactionGroupCategory.setTransactionCategory(transactionCategory);
            transactionGroupCategoryList.add(transactionGroupCategory);
        }
        transactionGroup.setTransactionGroupCategories(transactionGroupCategoryList);
        transactionGroupRepository.saveAndFlush(transactionGroup);
    }

    @Override
    public void deleteTransactionGroup(Long id) {
        transactionGroupRepository.delete(id);
    }

    @Transactional
    void deleteTransGrpCategory(Long id) {
        transGrpCategoryRepository.delete(id);
        transGrpCategoryRepository.flush();
    }
}
