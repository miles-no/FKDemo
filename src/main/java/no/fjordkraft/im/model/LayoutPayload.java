package no.fjordkraft.im.model;

import javax.persistence.*;

/**
 * Created by miles on 6/12/2017.
 */
@Entity
@Table(name="IM_LAYOUT_PAYLOAD")
public class LayoutPayload {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_LAYOUT_PAYLOAD_SEQ")
    private Long id;

    @ManyToOne(targetEntity=LayoutConfig.class, cascade=CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name="CONFIG_ID", nullable=false)
    private LayoutConfig layoutConfig;

    @Column(name="PAYLOAD", updatable=true)
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String payload;

    @Column(name="LAYOUT_VERSION")
    private int layoutVersion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getLayoutVersion() {
        return layoutVersion;
    }

    public void setLayoutVersion(int layoutVersion) {
        this.layoutVersion = layoutVersion;
    }

    public LayoutConfig getLayoutConfig() {
        return layoutConfig;
    }

    public void setLayoutConfig(LayoutConfig layoutConfig) {
        this.layoutConfig = layoutConfig;
    }
}
