package no.fjordkraft.im.controller;

import no.fjordkraft.im.domain.RestTransactionGroup;
import no.fjordkraft.im.model.TransactionCategory;
import no.fjordkraft.im.ui.services.UITransactionGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miles on 9/25/2017.
 */
@RestController
@RequestMapping("/api/custom/transaction")
public class IMTransactionGroupController {

    @Autowired
    UITransactionGroupService transactionGroupService;

    @RequestMapping(value = "group/all", method = RequestMethod.GET)
    @ResponseBody
    List<RestTransactionGroup> getTransactionGroup() {
        return transactionGroupService.getAll();
    }

    @RequestMapping(value = "group/{brandName}", method = RequestMethod.GET)
    @ResponseBody
    List<RestTransactionGroup> getTransactionGroupByBrand(@PathVariable(value = "brandName") String brandName) {
        return transactionGroupService.getTransactionGroupByBrand(brandName);
    }

    @RequestMapping(value = "group", method = RequestMethod.POST)
    @ResponseBody
    void saveTransactionGroup(@RequestBody RestTransactionGroup restTransactionGroup) {
        transactionGroupService.saveTransactionGroup(restTransactionGroup);
    }

    @RequestMapping(value = "group/{id}", method = RequestMethod.PUT)
    @ResponseBody
    void saveTransactionGroup(@PathVariable(value = "id") Long id,
                              @RequestBody RestTransactionGroup restTransactionGroup) {
        transactionGroupService.updateTransactionGroup(id, restTransactionGroup);
    }

    @RequestMapping(value = "group/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void deleteTransactionGroup(@PathVariable(value = "id") Long id) {
        transactionGroupService.deleteTransactionGroup(id);
    }

    @RequestMapping(value = "category", method = RequestMethod.GET)
    @ResponseBody
    List<TransactionCategory> getCategories() {
        return transactionGroupService.getAllTransactionCategories();
    }

    @RequestMapping(value = "category/{type}", method = RequestMethod.GET)
    @ResponseBody
    List<TransactionCategory> getCategoriesByType(@PathVariable(value = "type") String type) {
        return transactionGroupService.getTransactionCategoryOnType(type);
    }

    @RequestMapping(value = "type", method = RequestMethod.GET)
    @ResponseBody
    List<String> getCategoryTypes() {
        List<String> types = new ArrayList<>();
        types.add("Debit");
        types.add("Credit");
        return types;
    }

   /* @RequestMapping(value = "category", method = RequestMethod.PUT)
    @ResponseBody
    void saveTransactionCategory(@RequestBody RestTransactionCategory transactionCategory) {
        // transactionGroupService.createTransactionCategory(transactionCategory);
    }*/
}
