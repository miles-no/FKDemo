package no.fjordkraft.im.model;

import javax.persistence.*;

/**
 * Created by bhavi on 6/6/2017.
 */
@Table(name="IM_SYSTEM_BATCH_INPUT_PAYLOAD")
@Entity
public class SystemBatchInputPayload {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_SBI_PAYLOAD_SEQ")
    private Long id;

    @OneToOne(targetEntity=SystemBatchInput.class, cascade=CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name="SBI_ID")
    private SystemBatchInput systemBatchInput;

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

    public SystemBatchInput getSystemBatchInput() {
        return systemBatchInput;
    }

    public void setSystemBatchInput(SystemBatchInput systemBatchInput) {
        this.systemBatchInput = systemBatchInput;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
