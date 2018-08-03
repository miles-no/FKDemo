package no.fjordkraft.im.ui.services.impl;

import no.fjordkraft.im.domain.NameValuePair;
import no.fjordkraft.im.domain.RestRuleAttribute;
import no.fjordkraft.im.model.RuleAttributes;
import no.fjordkraft.im.repository.RuleAttributesRepository;
import no.fjordkraft.im.ui.services.UIRuleAttributesService;
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
public class UIRuleAttributesServiceImpl implements UIRuleAttributesService {

    @Autowired
    RuleAttributesRepository ruleAttributesRepository;

    @Override
    public List<RestRuleAttribute> getAllLayoutConfig() {
        List<RuleAttributes> ruleAttributesList = ruleAttributesRepository.findAll();
        List<RestRuleAttribute> restRuleAttributeList = new ArrayList<>();
        RestRuleAttribute restRuleAttribute;
        for(RuleAttributes ruleAttributes:ruleAttributesList) {
            restRuleAttribute = new RestRuleAttribute();
            restRuleAttribute.setId(ruleAttributes.getId());
            restRuleAttribute.setName(ruleAttributes.getName());
            restRuleAttribute.setType(ruleAttributes.getType());
            restRuleAttribute.setFieldMapping(ruleAttributes.getFieldMapping());
            if(null != ruleAttributes.getOptions()) {
                restRuleAttribute.setOptions(ruleAttributes.getOptions().split("\\s*,\\s*"));
            }
            restRuleAttributeList.add(restRuleAttribute);
        }
        return restRuleAttributeList;
    }

    @Override
    @Transactional
    public void saveLayoutConfig(RuleAttributes ruleAttributes) {
        ruleAttributes.setCreatedTms(new Timestamp(System.currentTimeMillis()));
        ruleAttributesRepository.saveAndFlush(ruleAttributes);
    }

    @Override
    @Transactional
    public void updateLayoutConfig(RuleAttributes ruleAttributes) {
        RuleAttributes config = ruleAttributesRepository.findOne(ruleAttributes.getId());
        config.setName(ruleAttributes.getName());
        config.setType(ruleAttributes.getType());
        config.setFieldMapping(ruleAttributes.getFieldMapping());
        config.setOptions(ruleAttributes.getOptions());
        config.setUpdateTms(new Timestamp(System.currentTimeMillis()));
        ruleAttributesRepository.saveAndFlush(config);
    }

    @Override
    public void deleteLayoutConfig(Long id) {
        ruleAttributesRepository.delete(id);
    }

    @Override
    public List<NameValuePair> getAllRuleAttributes() {
        List<RuleAttributes> ruleAttributesList = ruleAttributesRepository.findAll();
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        NameValuePair nameValuePair;
        for(RuleAttributes ruleAttributes:ruleAttributesList) {
            nameValuePair = new NameValuePair();
            nameValuePair.setName(ruleAttributes.getName());
            nameValuePair.setValue(ruleAttributes.getId());
            nameValuePairList.add(nameValuePair);
        }
        return nameValuePairList;
    }

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
