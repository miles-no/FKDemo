package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.domain.NameValuePair;
import no.fjordkraft.im.model.RuleAttributes;
import no.fjordkraft.im.repository.RuleAttributesRepository;
import no.fjordkraft.im.services.RuleAttributesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miles on 6/27/2017.
 */
@Service
public class RuleAttributesServiceImpl implements RuleAttributesService {

    @Autowired
    RuleAttributesRepository ruleAttributesRepository;

    @Override
    public List<RuleAttributes> getAllLayoutConfig() {
        return ruleAttributesRepository.findAll();
    }

    @Override
    public void saveLayoutConfig(RuleAttributes ruleAttributes) {
        ruleAttributesRepository.saveAndFlush(ruleAttributes);
    }

    @Override
    public void updateLayoutConfig(RuleAttributes ruleAttributes) {
        RuleAttributes config = ruleAttributesRepository.findOne(ruleAttributes.getId());
        config.setName(ruleAttributes.getName());
        config.setType(ruleAttributes.getType());
        config.setFieldMapping(ruleAttributes.getFieldMapping());
        ruleAttributesRepository.saveAndFlush(config);
    }

    @Override
    public void deleteLayoutConfig(Long id) {
        ruleAttributesRepository.delete(id);
    }

    @Override
    public RuleAttributes getRuleAttributeByName(String name) {
        return ruleAttributesRepository.getAttributeByName(name);
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
}
