package no.fjordkraft.im.model;

import javax.persistence.*;

/**
 * Created by miles on 6/27/2017.
 */
@Entity
@Table(name="IM_LAYOUT_RULE_MAP")
public class LayoutRuleMap {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_LAYOUT_RULE_MAP_SEQ")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="RULE_ID")
    private LayoutRule layoutRule;

    /*@Column(name="RULE_ID")
    private Long ruleId;*/

    @Column(name="NAME")
    private String name;

    @Column(name="OPERATION")
    private String operation;

    @Column(name="VALUE")
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*public LayoutRule getLayoutRule() {
        return layoutRule;
    }

    public void setLayoutRule(LayoutRule layoutRule) {
        this.layoutRule = layoutRule;
    }*/

    /*public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
