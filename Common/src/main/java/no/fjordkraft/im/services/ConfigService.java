package no.fjordkraft.im.services;

/**
 * Created by bhavi on 4/28/2017.
 */

import no.fjordkraft.im.model.Config;

import java.util.Map;
import java.util.Set;

public interface ConfigService {

    Config findById(String id);

    String getString(String key);

    Integer getInteger(String key);

    Boolean getBoolean(String key);

    Long getLong(String key);

    void clearCache();

    Map<String, Set<String>> getTransactionGroupForBrand(String brand);
}
