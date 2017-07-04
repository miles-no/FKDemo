package no.fjordkraft.im.domain;

import java.io.InputStream;

/**
 * Created by miles on 6/13/2017.
 */
public class RestLayoutTemplate {

    private String name;
    private String description;
    private String rptDesign;

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

    public String getRptDesign() {
        return rptDesign;
    }

    public void setRptDesign(String rptDesign) {
        this.rptDesign = rptDesign;
    }
}
