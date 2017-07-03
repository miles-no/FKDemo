package no.fjordkraft.im.services;

import no.fjordkraft.im.model.LayoutRule;

import java.util.List;

/**
 * Created by miles on 6/27/2017.
 */
public interface LayoutRuleService {

    List<LayoutRule> getAllLayoutRule();
    List<LayoutRule> getLayoutRuleByBrand(String brand);
    void saveLayoutRule(LayoutRule layoutRule);
    void updateLayoutRule(LayoutRule layoutRule);
    void deleteLayoutRule(Long id);
}
