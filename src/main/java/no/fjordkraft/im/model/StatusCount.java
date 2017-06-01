package no.fjordkraft.im.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

/**
 * Created by miles on 5/26/2017.
 */
public class StatusCount {

    private String name;
    private Long value;

    public StatusCount(){}

    public StatusCount(String name, Long value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
