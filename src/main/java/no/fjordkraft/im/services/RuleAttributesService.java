package no.fjordkraft.im.services;

import no.fjordkraft.im.domain.NameValuePair;
import no.fjordkraft.im.domain.RestRuleAttribute;
import no.fjordkraft.im.model.RuleAttributes;

import java.util.List;

/**
 * Created by miles on 6/27/2017.
 */
public interface RuleAttributesService {

    List<RestRuleAttribute> getAllLayoutConfig();
    void saveLayoutConfig(RuleAttributes ruleAttributes);
    void updateLayoutConfig(RuleAttributes ruleAttributes);
    void deleteLayoutConfig(Long id);
    RestRuleAttribute getRuleAttributeByName(String name);
    List<NameValuePair> getAllRuleAttributes();
}
