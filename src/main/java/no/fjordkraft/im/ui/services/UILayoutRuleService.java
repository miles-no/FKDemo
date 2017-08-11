package no.fjordkraft.im.ui.services;

import no.fjordkraft.im.model.LayoutRule;

import java.util.List;

/**
 * Created by miles on 8/10/2017.
 */
public interface UILayoutRuleService {

    void saveLayoutRule(LayoutRule layoutRule);
    void updateLayoutRule(LayoutRule layoutRule);
    List<LayoutRule> getAllLayoutRule();
    List<LayoutRule> getLayoutRuleByBrand(String brand);
    void deleteLayoutRule(Long id);
    List<LayoutRule> getLayoutRuleByLayout(Long layoutId);
}
