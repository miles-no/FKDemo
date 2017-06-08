package no.fjordkraft.im.domain;

import no.fjordkraft.im.model.Statement;

import javax.persistence.*;

/**
 * Created by bhavi on 6/8/2017.
 */
public class RestInvoicePdf {


    private Long id;


    private Long statementId;


    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStatementId() {
        return statementId;
    }

    public void setStatementId(Long statementId) {
        this.statementId = statementId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
