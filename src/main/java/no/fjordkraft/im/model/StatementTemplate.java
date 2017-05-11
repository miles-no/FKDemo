package no.fjordkraft.im.model;

import javax.persistence.*;
import java.sql.Clob;

/**
 * Created by miles on 5/2/2017.
 */

@Table(name="IM_STATEMENT_TEMPLATE")
@Entity
public class StatementTemplate {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_STATEMENT_TEMPLATE_SEQ")
    private Long id;
    @Column(name="TYPE")
    private String type;
    @Column(name="VALUE")
    private String value;
    @Column(name="TEMPLATE")
    private Clob template;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Clob getTemplate() {
        return template;
    }

    public void setTemplate(Clob template) {
        this.template = template;
    }
}
