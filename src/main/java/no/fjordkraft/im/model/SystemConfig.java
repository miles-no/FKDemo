package no.fjordkraft.im.model;

import javax.persistence.*;

/**
 * Created by miles on 5/2/2017.
 */
@Table(name="IM_SYSTEM_CONFIG")
@Entity
public class SystemConfig {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_SYSTEM_CONFIG_SEQ")
    private long id;
    @Column(name="NAME")
    private String name;
    @Column(name="VALUE")
    private String value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        id = id;
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
}
