package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.domain.RestLayout;
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
    public List<Layout> getAllLayoutDesign() {
        return layoutRepository.findAll();
    }

    @Override
    public Layout getLayoutDesignById(Long id) {
        return layoutRepository.findOne(id);
    }

    @Override
    public void saveLayoutDesign(Layout layout) {
        layout.setCreateTime(new Timestamp(System.currentTimeMillis()));
        layout.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        layoutRepository.saveAndFlush(layout);
    }

    @Override
    public void updateLayoutDesign(Layout layout) {
        Layout design = layoutRepository.findOne(layout.getId());
        design.setName(layout.getName());
        design.setDescription(layout.getDescription());
        design.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        layoutRepository.saveAndFlush(design);
    }

    @Override
    public void deleteLayoutDesign(Long id) {
        layoutRepository.delete(id);
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
                restLayout.setName(layout.getName());
                restLayout.setDescription(layout.getDescription());
                restLayout.setCreateTime(layout.getCreateTime());
                restLayout.setUpdateTime(layout.getUpdateTime());
                restLayout.setContentID(layoutContent.getId());
                restLayout.setVersion(layoutContent.getVersion());
                restLayoutList.add(restLayout);
            }
        }
        return restLayoutList;
    }

    /*@Override
    public Layout getLayoutByIdAndVersion(Long id, Integer version) {
        LayoutID layoutID = new LayoutID();
        layoutID.setId(id);
        layoutID.setVersion(version);
        return layoutDesignRepository.findOne(layoutID);
    }*/
}
