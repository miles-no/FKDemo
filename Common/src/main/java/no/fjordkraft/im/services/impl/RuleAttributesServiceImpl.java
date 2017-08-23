package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.domain.RestRuleAttribute;
import no.fjordkraft.im.model.RuleAttributes;
import no.fjordkraft.im.repository.RuleAttributesRepository;
import no.fjordkraft.im.services.RuleAttributesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by miles on 6/27/2017.
 */
@Service
public class RuleAttributesServiceImpl implements RuleAttributesService {

    @Autowired
    RuleAttributesRepository ruleAttributesRepository;

    @Override
    public RestRuleAttribute getRuleAttributeByName(String name) {
        RuleAttributes ruleAttributes = ruleAttributesRepository.getAttributeByName(name);
        RestRuleAttribute restRuleAttribute = new RestRuleAttribute();
        restRuleAttribute.setId(ruleAttributes.getId());
        restRuleAttribute.setName(ruleAttributes.getName());
        restRuleAttribute.setType(ruleAttributes.getType());
        restRuleAttribute.setFieldMapping(ruleAttributes.getFieldMapping());
        if(null != ruleAttributes.getOptions()) {
            restRuleAttribute.setOptions(ruleAttributes.getOptions().split("\\s*,\\s*"));
        }
        return restRuleAttribute;
    }

}
