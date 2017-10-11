package no.fjordkraft.im.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by bhavi on 10/10/2017.
 */
public class StatementsList {

    private List<Long> statementIds;

    @JsonCreator
    public StatementsList(@JsonProperty("statementIds") List<Long> statementIds) {
        this.statementIds = statementIds;
    }

    public List<Long> getStatementIds() {
        return statementIds;
    }

    public void setStatementIds(List<Long> statementIds) {
        this.statementIds = statementIds;
    }
}
