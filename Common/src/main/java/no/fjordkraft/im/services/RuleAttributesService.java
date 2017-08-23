package no.fjordkraft.im.services;

import no.fjordkraft.im.domain.RestRuleAttribute;

/**
 * Created by miles on 6/27/2017.
 */
public interface RuleAttributesService {

    RestRuleAttribute getRuleAttributeByName(String name);
}
