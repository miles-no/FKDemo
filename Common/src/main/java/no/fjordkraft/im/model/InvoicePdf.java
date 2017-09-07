package no.fjordkraft.im.model;

import javax.persistence.*;

/**
 * Created by bhavi on 5/11/2017.
 */
@Entity
@Table(name="IM_INVOICE_PDFS")
public class InvoicePdf {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_INVOICE_PDFS_SEQ")
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name="STATEMENT_ID")
    private Statement statement;

    @Column
    private String type;

    @Column(name="PAYLOAD")
    @Lob
    private byte[] payload;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statementId) {
        this.statement = statementId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
