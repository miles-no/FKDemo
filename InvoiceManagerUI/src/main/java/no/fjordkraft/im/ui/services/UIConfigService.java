package no.fjordkraft.im.ui.services;

import no.fjordkraft.im.model.Config;

import java.util.List;

/**
 * Created by miles on 8/10/2017.
 */
public interface UIConfigService {

    Config updateConfig(String key, String value);
    void saveConfig(String key, String value);
    void deleteConfig(String key);
    Long getConfigCount();
    List<Config> findAll();
    String getString(String key);
    Config findById(String id);
    void clearCache();
}
