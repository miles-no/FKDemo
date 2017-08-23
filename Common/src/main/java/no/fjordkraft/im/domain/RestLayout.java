package no.fjordkraft.im.domain;

import no.fjordkraft.im.model.LayoutRule;

import java.sql.Timestamp;

/**
 * Created by miles on 7/3/2017.
 */
public class RestLayout {

    private Long layoutID;
    private String brand;
    private String name;
    private String description;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Long contentID;
    private Integer version;
    private boolean active;
    private LayoutRule layoutRule;

    public Long getLayoutID() {
        return layoutID;
    }

    public void setLayoutID(Long layoutID) {
        this.layoutID = layoutID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreateTime() {
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

    public Long getContentID() {
        return contentID;
    }

    public void setContentID(Long contentID) {
        this.contentID = contentID;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LayoutRule getLayoutRule() {
        return layoutRule;
    }

    public void setLayoutRule(LayoutRule layoutRule) {
        this.layoutRule = layoutRule;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
