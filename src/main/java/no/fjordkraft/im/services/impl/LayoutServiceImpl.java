package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.domain.NameValuePair;
import no.fjordkraft.im.domain.RestLayout;
import no.fjordkraft.im.domain.RestLayoutTemplate;
import no.fjordkraft.im.model.Layout;
import no.fjordkraft.im.model.LayoutContent;
import no.fjordkraft.im.model.LayoutRule;
import no.fjordkraft.im.repository.LayoutRepository;
import no.fjordkraft.im.services.LayoutService;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by miles on 6/27/2017.
 */
@Service
public class LayoutServiceImpl implements LayoutService {

    @Autowired
    LayoutRepository layoutRepository;

    @Autowired
    LayoutContentServiceImpl layoutContentService;

    @Autowired
    LayoutRuleServiceImpl layoutRuleService;

    @Override
    public Layout getLayoutDesignById(Long id) {
        return layoutRepository.findOne(id);
    }

    @Override
    public Layout saveLayout(RestLayoutTemplate restLayoutTemplate) {
        Layout layout = new Layout();
        layout.setName(restLayoutTemplate.getName());
        layout.setDescription(restLayoutTemplate.getDescription());
        layout.setCreateTime(new Timestamp(System.currentTimeMillis()));
        layout.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        Layout saved = layoutRepository.saveAndFlush(layout);

        layoutContentService.saveLayoutContent(saved.getId(), restLayoutTemplate.getRptDesign());
        return layoutRepository.findOne(saved.getId());
    }

    @Override
    public Layout updateLayout(Long id, RestLayoutTemplate restLayoutTemplate) {
        Layout layout = layoutRepository.findOne(id);
        layout.setName(restLayoutTemplate.getName());
        layout.setDescription(restLayoutTemplate.getDescription());
        layout.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        layoutRepository.saveAndFlush(layout);
        layoutContentService.updateLayoutContent(id, restLayoutTemplate.getRptDesign());
        return layoutRepository.findOne(id);
    }

    @Override
    public String getRptDesignFile(Long layoutId) {
        LayoutContent layoutContent = layoutContentService.getLayoutContentByLayoutId(layoutId, Boolean.TRUE);
        return layoutContent.getFileContent();
    }

    @Override
    public List<RestLayout> getLayoutByBrand(String brand) {
        List<LayoutRule> layoutRuleList = layoutRuleService.getLayoutRuleByBrand(brand);
        List<RestLayout> restLayoutList = new ArrayList<>();
        RestLayout restLayout;
        Layout layout;
        LayoutContent layoutContent;
        for(LayoutRule layoutRule:layoutRuleList) {
            layout = getLayoutDesignById(layoutRule.getLayoutId());
            layoutContent = layoutContentService.getLayoutContentByLayoutId(layout.getId(), Boolean.TRUE);
            if(null != layout && null != layoutContent) {
                restLayout = new RestLayout();
                restLayout.setLayoutID(layout.getId());
                restLayout.setBrand(layoutRule.getBrand());
                restLayout.setName(layout.getName());
                restLayout.setDescription(layout.getDescription());
                restLayout.setCreateTime(layout.getCreateTime());
                restLayout.setUpdateTime(layout.getUpdateTime());
                restLayout.setContentID(layoutContent.getId());
                restLayout.setVersion(layoutContent.getVersion());
                restLayout.setLayoutRule(layoutRule);
                restLayoutList.add(restLayout);
            }
        }
        return restLayoutList;
    }

    @Override
    public List<RestLayout> getAllLayout() {
        List<LayoutRule> layoutRuleList = layoutRuleService.getAllLayoutRule();
        List<RestLayout> restLayoutList = new ArrayList<>();
        RestLayout restLayout;
        Layout layout;
        LayoutContent layoutContent;
        for(LayoutRule layoutRule:layoutRuleList) {
            layout = getLayoutDesignById(layoutRule.getLayoutId());
            layoutContent = layoutContentService.getLayoutContentByLayoutId(layout.getId(), Boolean.TRUE);
            if(null != layout && null != layoutContent) {
                restLayout = new RestLayout();
                restLayout.setLayoutID(layout.getId());
                restLayout.setBrand(layoutRule.getBrand());
                restLayout.setName(layout.getName());
                restLayout.setDescription(layout.getDescription());
                restLayout.setCreateTime(layout.getCreateTime());
                restLayout.setUpdateTime(layout.getUpdateTime());
                restLayout.setContentID(layoutContent.getId());
                restLayout.setVersion(layoutContent.getVersion());
                restLayout.setLayoutRule(layoutRule);
                restLayoutList.add(restLayout);
            }
        }
        return restLayoutList;
    }

    @Override
    public List<NameValuePair> getLayoutList() {
        List<Layout> layoutList = layoutRepository.findAll();
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        NameValuePair nameValuePair;
        for(Layout layout:layoutList) {
            nameValuePair = new NameValuePair();
            nameValuePair.setName(layout.getName());
            nameValuePair.setValue(layout.getId());
            nameValuePairs.add(nameValuePair);
        }
        return nameValuePairs;
    }

    /*@Override
    public Layout getLayoutByIdAndVersion(Long id, Integer version) {
        LayoutID layoutID = new LayoutID();
        layoutID.setId(id);
        layoutID.setVersion(version);
        return layoutDesignRepository.findOne(layoutID);
    }*/
}
