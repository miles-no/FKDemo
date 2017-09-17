package no.fjordkraft.im.ui.services;

import no.fjordkraft.im.model.BrandConfig;

import java.util.List;

/**
 * Created by miles on 8/10/2017.
 */
public interface UIBrandService {

    List<BrandConfig> getBrandConfigs();
    void saveBrandConfig(BrandConfig brandConfig);
    void updateBrandConfig(BrandConfig brandConfig);
    void deleteBrandConfig(Long id);
    BrandConfig getBrandConfigByName(String brand);
    List<String> getAllBrands();
    Long getBrandCount();
}
