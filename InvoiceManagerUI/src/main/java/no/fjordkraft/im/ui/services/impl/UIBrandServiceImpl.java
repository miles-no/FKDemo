package no.fjordkraft.im.ui.services.impl;

import no.fjordkraft.im.model.BrandConfig;
import no.fjordkraft.im.repository.BrandConfigRepository;
import no.fjordkraft.im.ui.services.UIBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by miles on 8/10/2017.
 */
@Service
public class UIBrandServiceImpl implements UIBrandService {

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
        brandConfig.setCreateTime(new Timestamp(System.currentTimeMillis()));
        brandConfigRepository.saveAndFlush(brandConfig);
    }

    @Override
    @Transactional
    public void updateBrandConfig(BrandConfig brandConfig) {
        BrandConfig config = new BrandConfig();
        config = brandConfigRepository.findOne(brandConfig.getId());
        config.setBrand(brandConfig.getBrand());
        config.setUseEABarcode(brandConfig.getUseEABarcode());
        config.setAgreementNumber(brandConfig.getAgreementNumber());
        config.setServiceLevel(brandConfig.getServiceLevel());
        config.setPrefixKID(brandConfig.getPrefixKID());
        config.setKontonummer(brandConfig.getKontonummer());
        config.setDescription(brandConfig.getDescription());
        config.setPostcode(brandConfig.getPostcode());
        config.setCity(brandConfig.getCity());
        config.setNationalId(brandConfig.getNationalId());
        config.setRegion(brandConfig.getRegion());
        config.setUpdateTime(new Timestamp(System.currentTimeMillis()));
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
