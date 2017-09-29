package no.fjordkraft.im.services.impl;

/**
 * Created by bhavi on 4/28/2017.
 */

import no.fjordkraft.im.model.Config;
import no.fjordkraft.im.model.TransactionGroup;
import no.fjordkraft.im.model.TransactionGroupCategory;
import no.fjordkraft.im.repository.ConfigRepository;
import no.fjordkraft.im.services.ConfigService;
import no.fjordkraft.im.services.TransactionGroupService;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

@Service("configService")
public class ConfigServiceImpl implements ConfigService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);

    public static final int CACHE_TIME = 60000;  // 60 secs

    private final Map<String, String> cache = new HashMap();

    private long lastCacheRefresh = 0;

    private final Map<String, Map<String, Set<String>>> transactionGroup = new HashMap<>();

    @Autowired
    @Resource
    public ConfigRepository configRepository;

    @Autowired
    TransactionGroupService transactionGroupService;

    @Autowired
    BrandServiceImpl brandService;

    @Transactional(readOnly = true)
    @Override
    public Config findById(String id) {
        return configRepository.findOne(id);
    }

    @PostConstruct
    public void constructTransGroup() {

        List<String> brandsList = brandService.getBrandsList();
        Map<String, Set<String>> transactionCategoryMap;
        Set<String> category;
        List<TransactionGroup> transactionGroupList;

        if(null != brandsList && IMConstants.ZERO != brandsList.size()) {
            for (String brand : brandsList) {
                transactionGroupList = transactionGroupService.getTransactionGroupByBrand(brand);

                if(null != transactionGroupList && IMConstants.ZERO != transactionGroupList.size()) {
                    transactionCategoryMap = new HashMap<>();
                    for (TransactionGroup transactionGroup : transactionGroupList) {
                        category = new HashSet<>();
                        for(TransactionGroupCategory transactionGroupCategory:transactionGroup.getTransactionGroupCategories()) {
                            category.add(transactionGroupCategory.getTransactionCategory().getCategory());
                        }
                        transactionCategoryMap.put(transactionGroup.getName(), category);
                    }
                    transactionGroup.put(brand, transactionCategoryMap);
                }
            }
        }
    }

    @Override
    public String getString(String key) {

        if (System.currentTimeMillis() > lastCacheRefresh + CACHE_TIME) {
            clearCache();
        }

        String cachedValue = cache.get(key);
        if (cachedValue != null) {
            return cachedValue;
        }

        Config config = findById(key);

        if (config != null) {
            synchronized (this) {
                cache.put(key, config.getValue());
            }
            return config.getValue();
        }
        return null;
    }


    @Override
    public Integer getInteger(String key) {
        String value = getString(key);
        if (value == null) {
            return null;
        }
        Integer ret = null;
        try {
            ret = new Integer(value);
        } catch (NumberFormatException e) {
            //add logger
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public Long getLong(String key) {
        String value = getString(key);
        if (value == null) {
            return null;
        }
        Long ret = null;
        try {
            ret = new Long(value);
        } catch (NumberFormatException e) {
            //add logger
            e.printStackTrace();
        }
        return ret;
    }


    @Override
    public Boolean getBoolean(String key) {
        String value = getString(key);
        if (value == null) {
            return false;
        }
        return Boolean.valueOf(value);
    }

    @Override
    public synchronized void clearCache() {
        cache.clear();
        lastCacheRefresh = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("configService: cache cleared");
        }
    }

    @Override
    public Map<String, Set<String>> getTransactionGroupForBrand(String brand) {
        return transactionGroup.get(brand);
    }

}
