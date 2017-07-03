package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.AccountCategory;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.model.LayoutRule;
import no.fjordkraft.im.model.LayoutRuleMap;
import no.fjordkraft.im.model.RuleAttributes;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.services.impl.LayoutRuleMapServiceImpl;
import no.fjordkraft.im.services.impl.LayoutRuleServiceImpl;
import no.fjordkraft.im.services.impl.RuleAttributesServiceImpl;
import no.fjordkraft.im.util.IMConstants;
import org.apache.poi.hssf.record.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.provider.certpath.CollectionCertStore;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Collections;

/**
 * Created by miles on 6/19/2017.
 */
@Service
@PreprocessorInfo(order=9)
public class LayoutSelectionPreprocessor extends BasePreprocessor {

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    LayoutRuleServiceImpl layoutRuleService;

    @Autowired
    RuleAttributesServiceImpl ruleAttributesService;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        Long layoutID = 0l;
        String statementBrand = request.getEntity().getSystemBatchInput().getBrand();
        Statement statement = request.getStatement();

        List<LayoutRule> layoutRules = layoutRuleService.getLayoutRuleByBrand(statementBrand);
        Collections.sort(layoutRules);
        String rulename;
        RuleAttributes ruleAttributes = null;
        boolean foundLayout = false;

        for(LayoutRule layoutRule:layoutRules) {
            for(LayoutRuleMap layoutRuleMap:layoutRule.getLayoutRuleMapList()) {
                rulename = layoutRuleMap.getName();
                ruleAttributes = ruleAttributesService.getRuleAttributeByName(rulename);
                String ruleAttributeType;

                Object value = PropertyUtils.getNestedProperty(statement, ruleAttributes.getFieldMapping());
                ruleAttributeType = String.valueOf(ruleAttributes.getType());

                if(null != value && "String".equals(ruleAttributeType)) {
                    if(IMConstants.EQUAL.equals(layoutRuleMap.getOperation())
                            && layoutRuleMap.getValue().equals(value.toString())) {
                        foundLayout = true;
                        continue;
                    } else if(IMConstants.NOT_EQUAL.equals(layoutRuleMap.getOperation())
                            && !layoutRuleMap.getValue().equals(value.toString())) {
                        foundLayout = true;
                        continue;
                    } else {
                        foundLayout = false;
                        break;
                    }
                } else if(null != value && (IMConstants.INTEGER.equals(ruleAttributeType) || IMConstants.FLOAT.equals(ruleAttributeType))) {
                    //Class classTemp = Class.forName("java.lang." + ruleAttributeType);
                    //Method method = classTemp.getMethod("valueOf", String.class);
                    int comparedResult = Float.valueOf(layoutRuleMap.getValue()).compareTo(Float.valueOf(value.toString()));

                    if(IMConstants.EQUAL.equals(layoutRuleMap.getOperation())
                            && IMConstants.ZERO == comparedResult) {
                        foundLayout = true;
                        continue;
                    } else if(IMConstants.NOT_EQUAL.equals(layoutRuleMap.getOperation())
                            && IMConstants.ZERO != comparedResult) {
                        foundLayout = true;
                        continue;
                    }else if(IMConstants.GREATER.equals(layoutRuleMap.getOperation())
                            && IMConstants.GREATER_THAN == comparedResult) {
                        foundLayout = true;
                        continue;
                    }else if(IMConstants.LESSER.equals(layoutRuleMap.getOperation())
                            && IMConstants.LESSER_THAN == comparedResult) {
                        foundLayout = true;
                        continue;
                    } else {
                        foundLayout = false;
                        break;
                    }
                }
            }
            if(foundLayout) {
                layoutID = layoutRule.getLayoutId();
                break;
            }
        }

        request.getEntity().setLayoutID(layoutID);
    }
}
