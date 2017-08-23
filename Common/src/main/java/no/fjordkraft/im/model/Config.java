package no.fjordkraft.im.model;

/**
 * Created by bhavi on 4/28/2017.
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "IM_CONFIG")
public class Config {

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    public Config() {
    }

    public Config(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("Config[");
        b.append("name=");
        b.append(name);
        b.append(", value=");
        b.append(value);
        b.append("]");
        return b.toString();
    }
}
