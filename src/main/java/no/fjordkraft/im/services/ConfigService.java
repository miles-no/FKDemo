package no.fjordkraft.im.services;

/**
 * Created by bhavi on 4/28/2017.
 */

import no.fjordkraft.im.model.Config;

import java.nio.charset.Charset;
import java.util.List;

public interface ConfigService {

    List<Config> findAll();

    Config findById(String id);

    String getString(String key);

    Integer getInteger(String key);

    Boolean getBoolean(String key);

}
