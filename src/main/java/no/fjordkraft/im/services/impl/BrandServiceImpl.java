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

    @Override
    @Transactional(readOnly=true)
    public BrandConfig getBrandConfigByName(String brand) {
        return brandConfigRepository.getBarcodeConfigByBrand(brand);
    }
}
