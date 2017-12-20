package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.domain.RestRuleAttribute;
import no.fjordkraft.im.exceptions.PreprocessorException;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.model.LayoutRule;
import no.fjordkraft.im.model.LayoutRuleMap;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.services.LayoutRuleService;
import no.fjordkraft.im.services.RuleAttributesService;
import no.fjordkraft.im.util.IMConstants;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

/**
 * Created by miles on 6/19/2017.
 */
@Service
@PreprocessorInfo(order=10)
public class LayoutSelectionPreprocessor extends BasePreprocessor {

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    LayoutRuleService layoutRuleService;

    @Autowired
    RuleAttributesService ruleAttributesService;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        Long layoutID = 0l;
        String statementBrand = request.getEntity().getSystemBatchInput().getTransferFile().getBrand();
        Statement statement = request.getStatement();
        boolean foundLayout = false;

        List<LayoutRule> layoutRules = layoutRuleService.getLayoutRuleByBrand(statementBrand);
        if(null != layoutRules) {
            Collections.sort(layoutRules);
            String rulename;
            RestRuleAttribute restRuleAttribute = null;

            for (LayoutRule layoutRule : layoutRules) {
                List<LayoutRuleMap> layoutRuleMapList = layoutRule.getLayoutRuleMapList();
                if(null == layoutRuleMapList || IMConstants.ZERO == layoutRuleMapList.size()) {
                    foundLayout = true;
                    layoutID = layoutRule.getLayoutId();
                    break;
                }
                for (LayoutRuleMap layoutRuleMap : layoutRule.getLayoutRuleMapList()) {
                    rulename = layoutRuleMap.getName();
                    restRuleAttribute = ruleAttributesService.getRuleAttributeByName(rulename);
                    String ruleAttributeType;
                    String field;

                    if (null != restRuleAttribute) {
                        field = getFieldMapped(restRuleAttribute.getFieldMapping());
                        try {
                            Object value = PropertyUtils.getNestedProperty(statement, field);
                            ruleAttributeType = String.valueOf(restRuleAttribute.getType());

                            if (null != value && IMConstants.STRING.equals(ruleAttributeType)) {
                                if (IMConstants.EQUALS.equals(layoutRuleMap.getOperation())
                                        && layoutRuleMap.getValue().equals(value.toString())) {
                                    foundLayout = true;
                                    continue;
                                } else if (IMConstants.NOT_EQUALS.equals(layoutRuleMap.getOperation())
                                        && !layoutRuleMap.getValue().equals(value.toString())) {
                                    foundLayout = true;
                                    continue;
                                } else {
                                    foundLayout = false;
                                    break;
                                }
                            } else if (null != value && (IMConstants.INTEGER.equals(ruleAttributeType) || IMConstants.FLOAT.equals(ruleAttributeType))) {
                                int comparedResult = Float.valueOf(layoutRuleMap.getValue()).compareTo(Float.valueOf(value.toString()));

                                if (IMConstants.EQUALS.equals(layoutRuleMap.getOperation())
                                        && IMConstants.ZERO == comparedResult) {
                                    foundLayout = true;
                                    continue;
                                } else if (IMConstants.NOT_EQUALS.equals(layoutRuleMap.getOperation())
                                        && IMConstants.ZERO != comparedResult) {
                                    foundLayout = true;
                                    continue;
                                } else if (IMConstants.GREATER_THAN.equals(layoutRuleMap.getOperation())
                                        && IMConstants.GREATER == comparedResult) {
                                    foundLayout = true;
                                    continue;
                                } else if (IMConstants.LESS_THAN.equals(layoutRuleMap.getOperation())
                                        && IMConstants.LESSER == comparedResult) {
                                    foundLayout = true;
                                    continue;
                                } else if (IMConstants.GREATER_THAN_EQUAL_TO.equals(layoutRuleMap.getOperation())
                                        && (IMConstants.GREATER == comparedResult || IMConstants.ZERO == comparedResult)) {
                                    foundLayout = true;
                                    continue;
                                } else if (IMConstants.LESS_THAN_EQUAL_TO.equals(layoutRuleMap.getOperation())
                                        && (IMConstants.LESSER == comparedResult || IMConstants.ZERO == comparedResult)) {
                                    foundLayout = true;
                                    continue;
                                } else {
                                    foundLayout = false;
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            throw new PreprocessorException("Error while reading rule value: " + field);
                        }
                    } else {
                        throw new PreprocessorException("Rule attribute not mapped: " + rulename);
                    }
                }
                if (foundLayout) {
                    layoutID = layoutRule.getLayoutId();
                    break;
                }
            }
        } else {
            throw new PreprocessorException("Layout Rules not found for brand or Brand is not mapped: " + statementBrand);
        }

        if(!foundLayout) {
            throw new PreprocessorException("Layout not found");
        }
        request.getEntity().setLayoutID(layoutID);
    }

    private String getFieldMapped(String fieldMapping) {
        StringBuffer result = new StringBuffer();
        String temp;
        if(fieldMapping.contains(".")) {
            String[] fields = fieldMapping.split("\\.");
            int i = 0;
            for (String field : fields) {
                if (IMConstants.ZERO != i) {
                    result.append(".");
                }
                temp = field.substring(0, 1).toLowerCase();
                temp += field.substring(1);
                result.append(temp);
                i++;
            }
            return result.toString();
        }

        temp = fieldMapping.substring(0, 1).toLowerCase();
        temp += fieldMapping.substring(1);
        return temp;
    }

    public void setLayoutRuleServiceImpl(LayoutRuleService layoutRuleService) {
        this.layoutRuleService = layoutRuleService;
    }
}
