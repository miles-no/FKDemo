package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.LayoutConfig;
import no.fjordkraft.im.model.LayoutPayload;
import no.fjordkraft.im.domain.RestLayoutConfig;
import no.fjordkraft.im.repository.LayoutConfigRepository;
import no.fjordkraft.im.services.LayoutConfigService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by miles on 6/13/2017.
 */
@Service
public class LayoutConfigServiceImpl implements LayoutConfigService {

    @Autowired
    LayoutConfigRepository layoutConfigRepository;

    @Override
    @Transactional
    public void saveLayoutConfig(RestLayoutConfig restLayoutConfig) throws IOException {
        List<LayoutPayload> layoutPayloadList = new ArrayList<>();
        LayoutPayload layoutPayload = new LayoutPayload();
        LayoutConfig layoutConfig = new LayoutConfig();

        //File file = new File(restLayoutConfig.getFilePath());
        //String rptDesign = FileUtils.readFileToString(file, StandardCharsets.ISO_8859_1);

        String rptDesign = IOUtils.toString(restLayoutConfig.getInputStream(), StandardCharsets.ISO_8859_1);

        layoutConfig.setBrand(restLayoutConfig.getBrand());
        layoutConfig.setLegalPartClass(restLayoutConfig.getLegalPartClass());
        layoutConfig.setAccountCategory(restLayoutConfig.getAccountCategory());
        layoutConfig.setDistributionMethod(restLayoutConfig.getDistributionMethod());
        layoutConfig.setCreditLimit(restLayoutConfig.isCreditLimit());

        layoutPayload.setLayoutConfig(layoutConfig);
        layoutPayload.setLayoutVersion(restLayoutConfig.getVersion());
        layoutPayload.setPayload(rptDesign);
        layoutPayloadList.add(layoutPayload);

        layoutConfig.setLayoutPayload(layoutPayloadList);

        layoutConfigRepository.saveAndFlush(layoutConfig);
    }

    @Override
    @Transactional
    public void updateLayoutConfig(RestLayoutConfig restLayoutConfig) throws IOException {

        LayoutConfig layoutConfig = layoutConfigRepository.findOne(restLayoutConfig.getId());
        List<LayoutPayload> layoutPayloadList = layoutConfig.getLayoutPayload();
        LayoutPayload layoutPayload = layoutPayloadList.get(0);

        String rptDesign = IOUtils.toString(restLayoutConfig.getInputStream(), StandardCharsets.ISO_8859_1);

        layoutConfig.setBrand(restLayoutConfig.getBrand());
        layoutConfig.setLegalPartClass(restLayoutConfig.getLegalPartClass());
        layoutConfig.setAccountCategory(restLayoutConfig.getAccountCategory());
        layoutConfig.setDistributionMethod(restLayoutConfig.getDistributionMethod());
        layoutConfig.setCreditLimit(restLayoutConfig.isCreditLimit());

        layoutPayload.setLayoutConfig(layoutConfig);
        layoutPayload.setLayoutVersion(restLayoutConfig.getVersion());
        layoutPayload.setPayload(rptDesign);
        layoutPayloadList.add(layoutPayload);

        layoutConfig.setLayoutPayload(layoutPayloadList);

        layoutConfigRepository.saveAndFlush(layoutConfig);
    }

    @Override
    @Transactional
    public void updateNewLayoutVersion(RestLayoutConfig restLayoutConfig) throws IOException {
        LayoutConfig layoutConfig = layoutConfigRepository.findOne(restLayoutConfig.getId());
        List<LayoutPayload> layoutPayloadList = layoutConfig.getLayoutPayload();
        LayoutPayload layoutPayload = new LayoutPayload();

        String rptDesign = IOUtils.toString(restLayoutConfig.getInputStream(), StandardCharsets.ISO_8859_1);

        layoutConfig.setBrand(restLayoutConfig.getBrand());
        layoutConfig.setLegalPartClass(restLayoutConfig.getLegalPartClass());
        layoutConfig.setAccountCategory(restLayoutConfig.getAccountCategory());
        layoutConfig.setDistributionMethod(restLayoutConfig.getDistributionMethod());
        layoutConfig.setCreditLimit(restLayoutConfig.isCreditLimit());

        layoutPayload.setLayoutConfig(layoutConfig);
        layoutPayload.setLayoutVersion(restLayoutConfig.getVersion());
        layoutPayload.setPayload(rptDesign);
        layoutPayloadList.add(layoutPayload);

        layoutConfig.setLayoutPayload(layoutPayloadList);

        layoutConfigRepository.saveAndFlush(layoutConfig);
    }

    @Override
    public String getRptDesignFile() {
        LayoutConfig layoutConfig = layoutConfigRepository.findOne(8l);
        List<LayoutPayload> layoutPayload = layoutConfig.getLayoutPayload();
        return layoutPayload.get(0).getPayload();
    }

    @Override
    public String getRptDesignFileByBrand(String brand) {
        LayoutConfig layoutConfig = layoutConfigRepository.getLayoutConfigByBrand(brand);
        List<LayoutPayload> layoutPayload = layoutConfig.getLayoutPayload();
        return layoutPayload.get(0).getPayload();
    }


}
