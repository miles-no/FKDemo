package no.fjordkraft.im.model;

/**
 * Created by bhavi on 4/28/2017.
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "config")
public class Config {

    @Id
    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    public Config() {
    }

    public Config(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
        b.append("key=");
        b.append(key);
        b.append(", value=");
        b.append(value);
        b.append("]");
        return b.toString();
    }
}
