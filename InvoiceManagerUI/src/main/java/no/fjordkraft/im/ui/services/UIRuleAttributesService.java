package no.fjordkraft.im.ui.services;

import no.fjordkraft.im.domain.NameValuePair;
import no.fjordkraft.im.domain.RestRuleAttribute;
import no.fjordkraft.im.model.RuleAttributes;

import java.util.List;

/**
 * Created by miles on 8/10/2017.
 */
public interface UIRuleAttributesService {

    List<RestRuleAttribute> getAllLayoutConfig();
    void saveLayoutConfig(RuleAttributes ruleAttributes);
    void updateLayoutConfig(RuleAttributes ruleAttributes);
    void deleteLayoutConfig(Long id);
    List<NameValuePair> getAllRuleAttributes();
    RestRuleAttribute getRuleAttributeByName(String name);
}
