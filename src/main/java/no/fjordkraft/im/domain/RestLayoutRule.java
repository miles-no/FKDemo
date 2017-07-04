package no.fjordkraft.im.domain;

import java.util.List;

/**
 * Created by miles on 7/4/2017.
 */
public class RestLayoutRule {

    private String brand;
    private Long layoutId;
    private List<RestLayoutRuleMap> ruleMapList;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    public List<RestLayoutRuleMap> getRuleMapList() {
        return ruleMapList;
    }

    public void setRuleMapList(List<RestLayoutRuleMap> ruleMapList) {
        this.ruleMapList = ruleMapList;
    }
}
