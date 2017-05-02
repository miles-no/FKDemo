package no.fjordkraft.im.services.impl;

/**
 * Created by bhavi on 4/28/2017.
 */

import no.fjordkraft.im.repository.ConfigRepository;
import no.fjordkraft.im.model.Config;
import no.fjordkraft.im.services.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("configService")
public class ConfigServiceImpl implements ConfigService {

    private final Map<String, String> cache = new HashMap();

    @Autowired
    @Resource
    public ConfigRepository configRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Config> findAll() {
        return configRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Config findById(String id) {
        return configRepository.findOne(id);
    }


    @Override
    public String getString(String key) {

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
    public Boolean getBoolean(String key) {
        String value = getString(key);
        if (value == null) {
            return null;
        }
        return Boolean.valueOf(value);
    }

}
