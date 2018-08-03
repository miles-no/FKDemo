package no.fjordkraft.im.ui.services.impl;

import no.fjordkraft.im.domain.NameValuePair;
import no.fjordkraft.im.domain.RestLayout;
import no.fjordkraft.im.domain.RestLayoutTemplate;
import no.fjordkraft.im.model.Layout;
import no.fjordkraft.im.model.LayoutContent;
import no.fjordkraft.im.model.LayoutRule;
import no.fjordkraft.im.repository.LayoutRepository;
import no.fjordkraft.im.ui.services.UILayoutContentService;
import no.fjordkraft.im.ui.services.UILayoutRuleService;
import no.fjordkraft.im.ui.services.UILayoutService;
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
public class UILayoutServiceImpl implements UILayoutService {

    @Autowired
    LayoutRepository layoutRepository;

    @Autowired
    UILayoutContentService layoutContentService;

    @Autowired
    UILayoutRuleService layoutRuleService;

    @Override
    @Transactional
    public Layout saveLayout(RestLayoutTemplate restLayoutTemplate) {
        Layout layout = new Layout();
        layout.setName(restLayoutTemplate.getName());
        layout.setDescription(restLayoutTemplate.getDescription());
        layout.setCreateTime(new Timestamp(System.currentTimeMillis()));
        Layout saved = layoutRepository.saveAndFlush(layout);

        layoutContentService.saveLayoutContent(saved.getId(), restLayoutTemplate.getRptDesign());
        return layoutRepository.findOne(saved.getId());
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
            layoutContent = layoutContentService.getLayoutContentByLayoutId(layout.getId());
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
            layoutContent = layoutContentService.getLayoutContentByLayoutId(layout.getId());
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
                restLayout.setActive(layoutContent.isActive());
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

    @Override
    public void deleteLayout(Long id) {
        List<LayoutContent> layoutContentList = layoutContentService.getAllLayoutContentByLayoutId(id);
        List<LayoutRule> layoutRuleList = layoutRuleService.getLayoutRuleByLayout(id);
        for(LayoutRule layoutRule:layoutRuleList) {
            layoutRuleService.deleteLayoutRule(layoutRule.getId());
        }
        for(LayoutContent layoutContent:layoutContentList) {
            layoutContentService.deleteLayoutContent(layoutContent.getId());
        }
        layoutRepository.delete(id);
    }

    @Override
    public Layout updateLayoutVersion(Long id, String template) {
        Layout layout = layoutRepository.findOne(id);
        if(null != layout) {
            layout.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            layoutRepository.saveAndFlush(layout);
            layoutContentService.updateLayoutVersion(id, template);
        }
        return layout;
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
    public Layout getLayoutDesignById(Long id) {
        return layoutRepository.findOne(id);
    }
}
