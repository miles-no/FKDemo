package no.fjordkraft.im.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by miles on 6/27/2017.
 */
@Entity
@Table(name="IM_RULE_ATTRIBUTES")
public class RuleAttributes {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_RULE_ATTRIBUTES_SEQ")
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name="TYPE")
    private String type;

    @Column(name="FIELD_MAPPING")
    private String fieldMapping;

    @Column(name="OPTIONS")
    private String options;

    @Column(name="CREATED_TMS")
    private Timestamp createdTms;

    @Column(name="UPDATE_TMS")
    private Timestamp updateTms;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFieldMapping() {
        return fieldMapping;
    }

    public void setFieldMapping(String fieldMapping) {
        this.fieldMapping = fieldMapping;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Timestamp getCreatedTms() {
        return createdTms;
    }

    public void setCreatedTms(Timestamp createdTms) {
        this.createdTms = createdTms;
    }

    public Timestamp getUpdateTms() {
        return updateTms;
    }

    public void setUpdateTms(Timestamp updateTms) {
        this.updateTms = updateTms;
    }
}
