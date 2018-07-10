package no.fjordkraft.im.model;

import javax.persistence.*;

/**
 * Created by bhavi on 6/6/2017.
 */

@Table(name="IM_STATEMENT_PAYLOAD")
@Entity
public class StatementPayload {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_STATEMENT_PAYLOAD_SEQ")
    private Long id;

    @OneToOne(targetEntity=Statement.class, cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="STATEMENT_ID")
    private Statement statement;

    @Column(name="PAYLOAD", updatable=false)
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String payload;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
