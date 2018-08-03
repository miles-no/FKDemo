package no.fjordkraft.im.ui.services.impl;

import no.fjordkraft.im.model.Config;
import no.fjordkraft.im.repository.ConfigRepository;
import no.fjordkraft.im.ui.services.UIConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by miles on 8/10/2017.
 */
@Service
public class UIConfigServiceImpl implements UIConfigService  {

    @Autowired
    @Resource
    public ConfigRepository configRepository;

    public static final int CACHE_TIME = 60000;  // 60 secs

    private final Map<String, String> cache = new HashMap();

    private long lastCacheRefresh = 0;

    @Override
    @Transactional
    public Config updateConfig(String key, String value) {
        Config configDb = configRepository.findOne(key);
        if (configDb == null) {
            configDb = new Config();
            configDb.setName(key);
        }
        configDb.setValue(value);
        configDb.setLastUpdated(new Timestamp(System.currentTimeMillis()));
        synchronized (this) {
            cache.put(key, value);
        }
        return configRepository.saveAndFlush(configDb);
    }

    @Override
    public void saveConfig(String key, String value) {
        Config config = new Config();
        config.setName(key);
        config.setValue(value);
        config.setCreatedTms(new Timestamp(System.currentTimeMillis()));
        configRepository.save(config);
    }

    @Override
    public void deleteConfig(String key) {
        configRepository.delete(key);
    }

    @Override
    public Long getConfigCount() {
        return configRepository.getConfigCount();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Config> findAll() {
        return configRepository.findAll();
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

    @Transactional(readOnly = true)
    @Override
    public Config findById(String id) {
        return configRepository.findOne(id);
    }

    @Override
    public synchronized void clearCache() {
        cache.clear();
        lastCacheRefresh = System.currentTimeMillis();
    }
}
