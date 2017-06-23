package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.BrandConfig;
import no.fjordkraft.im.repository.BrandConfigRepository;
import no.fjordkraft.im.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by miles on 6/5/2017.
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    BrandConfigRepository brandConfigRepository;

    @Transactional(readOnly=true)
    @Override
    public List<BrandConfig> getBrandConfigs() {
        List<BrandConfig> brandConfigList = new ArrayList<>();
        brandConfigList = brandConfigRepository.getBrandConfigs();
        return brandConfigList;
    }

    @Override
    @Transactional
    public void saveBrandConfig(BrandConfig brandConfig) {
        brandConfigRepository.saveAndFlush(brandConfig);
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteBrandConfig(Long id) {
        brandConfigRepository.delete(id);
    }

    @Override
    @Transactional(readOnly=true)
    public BrandConfig getBrandConfigByName(String brand) {
        return brandConfigRepository.getBarcodeConfigByBrand(brand);
    }

    @Override
    public List<String> getAllBrands() {
        return brandConfigRepository.getAllBrands();
    }

    @Override
    public Long getBrandCount() {
        return brandConfigRepository.getBrandsCount();
    }
}
