package no.fjordkraft.im.ui.services.impl;

import no.fjordkraft.im.model.LayoutRule;
import no.fjordkraft.im.repository.LayoutRuleRepository;
import no.fjordkraft.im.ui.services.UILayoutRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by miles on 8/10/2017.
 */
@Service
public class UILayoutRuleServiceImpl implements UILayoutRuleService {

    @Autowired
    LayoutRuleRepository layoutRuleRepository;

    @Override
    public void saveLayoutRule(LayoutRule layoutRule) {
        layoutRule.setCreatedTms(new Timestamp(System.currentTimeMillis()));
        layoutRuleRepository.saveAndFlush(layoutRule);
    }

    @Override
    public void updateLayoutRule(LayoutRule layoutRule) {
        LayoutRule rule = layoutRuleRepository.findOne(layoutRule.getId());
        rule.setBrand(layoutRule.getBrand());
        rule.setLayoutId(layoutRule.getLayoutId());
        rule.setUpdatedTms(new Timestamp(System.currentTimeMillis()));
        rule.setLayoutRuleMapList(layoutRule.getLayoutRuleMapList());
        layoutRuleRepository.saveAndFlush(rule);
    }

    @Override
    public List<LayoutRule> getAllLayoutRule() {
        return layoutRuleRepository.findAll();
    }

    @Override
    public List<LayoutRule> getLayoutRuleByBrand(String brand) {
        return layoutRuleRepository.getLayoutRuleByBrand(brand);
    }

    @Override
    public void deleteLayoutRule(Long id) {
        layoutRuleRepository.delete(id);
    }

    @Override
    public List<LayoutRule> getLayoutRuleByLayout(Long layoutId) {
        return layoutRuleRepository.getLayoutRuleByLayout(layoutId);
    }
}
