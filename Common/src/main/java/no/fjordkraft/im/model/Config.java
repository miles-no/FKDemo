package no.fjordkraft.im.model;

/**
 * Created by bhavi on 4/28/2017.
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "IM_CONFIG")
public class Config {

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @Column(name = "created_tms")
    private Timestamp createdTms;

    @Column(name = "Last_Updated")
    private Timestamp lastUpdated;

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

    public Timestamp getCreatedTms() {
        return createdTms;
    }

    public void setCreatedTms(Timestamp createdTms) {
        this.createdTms = createdTms;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
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
