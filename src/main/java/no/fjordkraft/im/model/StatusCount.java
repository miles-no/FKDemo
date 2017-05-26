package no.fjordkraft.im.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

/**
 * Created by miles on 5/26/2017.
 */
public class StatusCount {

    private String status;
    private Long count;

    public StatusCount(){}

    public StatusCount(String status, Long count) {
        this.status = status;
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
