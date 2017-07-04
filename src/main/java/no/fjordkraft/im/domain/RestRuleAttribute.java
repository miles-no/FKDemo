package no.fjordkraft.im.domain;

/**
 * Created by miles on 7/4/2017.
 */
public class RestRuleAttribute {

    private String name;
    private String type;
    private String fieldMapping;

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
}
