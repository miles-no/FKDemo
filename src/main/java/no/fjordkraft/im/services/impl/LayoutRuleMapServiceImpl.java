package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.LayoutRuleMap;
import no.fjordkraft.im.repository.LayoutRuleMapRepository;
import no.fjordkraft.im.services.LayoutRuleMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by miles on 6/27/2017.
 */
@Service
public class LayoutRuleMapServiceImpl implements LayoutRuleMapService {

    @Autowired
    LayoutRuleMapRepository layoutRuleMapRepository;

    @Override
    public List<LayoutRuleMap> getAllLayoutRuleMap() {
        return layoutRuleMapRepository.findAll();
    }

    @Override
    public void saveLayoutRuleMap(LayoutRuleMap layoutRuleMap) {
        layoutRuleMapRepository.saveAndFlush(layoutRuleMap);
    }

    @Override
    public void updateLayoutRuleMap(LayoutRuleMap layoutRuleMap) {
        LayoutRuleMap ruleMap = layoutRuleMapRepository.findOne(layoutRuleMap.getId());
        ruleMap.setRuleId(layoutRuleMap.getRuleId());
        ruleMap.setName(layoutRuleMap.getName());
        ruleMap.setOperation(layoutRuleMap.getOperation());
        ruleMap.setValue(layoutRuleMap.getValue());
        layoutRuleMapRepository.saveAndFlush(ruleMap);
    }

    @Override
    public void deleteLayoutRuleMap(Long id) {
        layoutRuleMapRepository.delete(id);
    }
}
