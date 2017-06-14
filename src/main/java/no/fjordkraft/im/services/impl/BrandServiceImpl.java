package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.BrandConfig;
import no.fjordkraft.im.repository.BrandConfigRepository;
import no.fjordkraft.im.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by miles on 6/5/2017.
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    BrandConfigRepository brandConfigRepository;

    @Transactional
    @Override
    public List<BrandConfig> getBrandConfigs() {
        List<BrandConfig> brandConfigList = new ArrayList<>();
        brandConfigList = brandConfigRepository.getBrandConfigs();
        return brandConfigList;
    }

    @Override
    public void saveBrandConfig(BrandConfig brandConfig) {
        brandConfigRepository.saveAndFlush(brandConfig);
    }

    @Override
    public void updateBrandConfig(BrandConfig brandConfig) {
        BrandConfig config = new BrandConfig();
        config = brandConfigRepository.findOne(brandConfig.getId());
        config.setUseEABarcode(brandConfig.getUseEABarcode());
        config.setAgreementNumber(brandConfig.getAgreementNumber());
        config.setServiceLevel(brandConfig.getServiceLevel());
        config.setPrefixKID(brandConfig.getPrefixKID());
        config.setKontonummer(brandConfig.getKontonummer());
        brandConfigRepository.saveAndFlush(config);
    }

    @Override
    public void deleteBrandConfig(Long id) {
        brandConfigRepository.delete(id);
    }

    @Override
    public BrandConfig getBrandConfigByName(String brand) {
        return brandConfigRepository.getBarcodeConfigByBrand(brand);
    }
}
