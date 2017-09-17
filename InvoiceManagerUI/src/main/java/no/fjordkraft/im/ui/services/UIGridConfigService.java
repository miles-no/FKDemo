package no.fjordkraft.im.ui.services;

import no.fjordkraft.im.model.GridConfig;

import java.util.List;

/**
 * Created by miles on 8/10/2017.
 */
public interface UIGridConfigService {

    List<GridConfig> getGridConfigs();
    void saveGridConfig(GridConfig gridConfig);
    void updateGridConfig(GridConfig gridConfig);
    void deleteGridConfig(Long id);
    Long getGridCount();
    List<String> getAllGrids();
    GridConfig getGridConfigByBrand(String brand);
}
