package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.LayoutRule;
import no.fjordkraft.im.repository.LayoutRuleRepository;
import no.fjordkraft.im.services.LayoutRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by miles on 6/27/2017.
 */
@Service
public class LayoutRuleServiceImpl implements LayoutRuleService {

    @Autowired
    LayoutRuleRepository layoutRuleRepository;

    @Override
    public List<LayoutRule> getAllLayoutRule() {
        return layoutRuleRepository.findAll();
    }

    @Override
    public List<LayoutRule> getLayoutRuleByBrand(String brand) {
        return layoutRuleRepository.getLayoutRuleByBrand(brand);
    }
}
