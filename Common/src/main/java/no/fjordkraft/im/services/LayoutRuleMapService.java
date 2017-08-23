package no.fjordkraft.im.services;

import no.fjordkraft.im.model.LayoutRuleMap;

import java.util.List;

/**
 * Created by miles on 6/27/2017.
 */
public interface LayoutRuleMapService {

    List<LayoutRuleMap> getAllLayoutRuleMap();
    void saveLayoutRuleMap(LayoutRuleMap layoutRuleMap);
    void updateLayoutRuleMap(LayoutRuleMap layoutRuleMap);
    void deleteLayoutRuleMap(Long id);
}
