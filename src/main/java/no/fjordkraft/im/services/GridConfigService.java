package no.fjordkraft.im.services;

import no.fjordkraft.im.model.GridConfig;

import java.util.List;

/**
 * Created by miles on 6/12/2017.
 */
public interface GridConfigService {

    List<GridConfig> getGridConfigs();
    void saveGridConfig(GridConfig gridConfig);
    void updateGridConfig(GridConfig gridConfig);
    void deleteGridConfig(Long id);
    GridConfig getGridConfigByBrand(String brand);
    Long getGridCount();
    List<String> getAllBrands();
}
