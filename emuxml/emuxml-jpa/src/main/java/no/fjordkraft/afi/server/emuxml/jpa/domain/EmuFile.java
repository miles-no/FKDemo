package no.fjordkraft.afi.server.emuxml.jpa.domain;

import lombok.Data;
import no.fjordkraft.afi.server.iscuclient.jpa.domain.InvoiceReconciliation;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "emu_file")
public class EmuFile {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "EFSEQ")
    @SequenceGenerator(name = "EFSEQ", sequenceName = "emu_file_id_seq")
    private Long id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "reportid")
    private String reportId;

    @Column(name = "orderno")
    private String orderNo;

    @Column(name = "productiondate")
    private Date productionDate;

    @Column(name = "numberofinvoices")
    private Long numberOfInvoices;

    @Column(name = "sumofgiroamount")
    private BigDecimal sumOfGiroAmount;

    @Column(name = "sumofprintedamount")
    private BigDecimal sumOfPrintedAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EmuFileStatusEnum status;

    @Column(name = "scanned")
    private Timestamp scanned;

    @Transient
    Long fileSize;

    @Transient
    InvoiceReconciliation invoiceReconciliation;

    public EmuFile() {
    }

    public EmuFile(String filename, long created, Long fileSize) {
        setFilename(filename);
        setProductionDate(new Date(new Timestamp(created).getTime()));
        setFileSize(fileSize);
        setStatus(EmuFileStatusEnum.created);
    }

    public void addGiroAmount(BigDecimal val) {
        sumOfGiroAmount = (sumOfGiroAmount != null ? sumOfGiroAmount : BigDecimal.ZERO).add(val);
    }

    public void addPrintedAmount(BigDecimal val) {
        sumOfPrintedAmount = (sumOfPrintedAmount != null ? sumOfPrintedAmount : BigDecimal.ZERO).add(val);
    }

}