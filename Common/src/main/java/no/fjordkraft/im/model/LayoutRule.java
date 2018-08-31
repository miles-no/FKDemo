package no.fjordkraft.im.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by miles on 6/27/2017.
 */
@Entity
@Table(name="IM_LAYOUT_RULE")
public class LayoutRule implements Comparable<LayoutRule> {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_LAYOUT_RULE_SEQ")
    private Long id;

    @Column(name="BRAND")
    private String brand;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="RULE_ID")
    private List<LayoutRuleMap> layoutRuleMapList;

    @Column(name="LAYOUT_ID")
    private Long layoutId;

    @Column(name="CREATE_TIME")
    private Timestamp createTime;

    @Column(name="UPDATE_TIME")
    private Timestamp updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Timestamp getcreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public List<LayoutRuleMap> getLayoutRuleMapList() {
        return layoutRuleMapList;
    }

    public void setLayoutRuleMapList(List<LayoutRuleMap> layoutRuleMapList) {
        this.layoutRuleMapList = layoutRuleMapList;
    }

    @Override
    public int compareTo(LayoutRule o) {
        if(null != this.getLayoutRuleMapList() && null != o.getLayoutRuleMapList()) {
            return Integer.valueOf(o.getLayoutRuleMapList().size()).compareTo(this.getLayoutRuleMapList().size());
        }
        return 0;
    }
}
