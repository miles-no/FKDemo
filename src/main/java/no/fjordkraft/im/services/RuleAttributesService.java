package no.fjordkraft.im.services;

import no.fjordkraft.im.model.RuleAttributes;

import java.util.List;

/**
 * Created by miles on 6/27/2017.
 */
public interface RuleAttributesService {

    List<RuleAttributes> getAllLayoutConfig();
    void saveLayoutConfig(RuleAttributes ruleAttributes);
    void updateLayoutConfig(RuleAttributes ruleAttributes);
    void deleteLayoutConfig(Long id);
    RuleAttributes getRuleAttributeByName(String name);
}
