package no.fjordkraft.im.services;

import no.fjordkraft.im.model.BrandConfig;
import no.fjordkraft.im.services.impl.IMBrandServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by miles on 6/5/2017.
 */
@Service
public interface IMBrandService {

    List<BrandConfig> getBrandConfigs();
    void saveBrandConfig(BrandConfig brandConfig);
    void updateBrandConfig(BrandConfig brandConfig);
    void deleteBrandConfig(Long id);
    BrandConfig getBrandConfigByName(String brand);
}
