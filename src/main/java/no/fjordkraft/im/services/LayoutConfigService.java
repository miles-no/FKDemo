package no.fjordkraft.im.services;

import no.fjordkraft.im.domain.RestLayoutConfig;

import java.io.IOException;

/**
 * Created by miles on 6/13/2017.
 */
public interface LayoutConfigService {

    void saveLayoutConfig(RestLayoutConfig restLayoutConfig) throws IOException;
    void updateLayoutConfig(RestLayoutConfig restLayoutConfig) throws IOException;
    void updateNewLayoutVersion(RestLayoutConfig restLayoutConfig) throws IOException;
    String getRptDesignFile();
}
