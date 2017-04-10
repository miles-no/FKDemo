package no.fjordkraft.afi.server.emuxml.jpa.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Data
@Entity
@Table(name = "emu_invoice")
public class EmuInvoice {

    @Id
    @Column(name = "invoiceNo")
    private Long invoiceNo;

    @Column(name = "ef_id")
    private Long ef_id;

    @Column(name = "customerId")
    private String customerId;

    @Column(name = "brand")
    private String brand;

    @Column(name = "accountNo")
    private String accountNo;

    @Column(name = "invAgreementId")
    private String invAgreementId;

    @Column(name = "printDate")
    private Date printDate;

    @Column(name = "giroAmount")
    private BigDecimal giroAmount;

    @Column(name = "printedAmount")
    private BigDecimal printedAmount;

    @Column(name = "attachmentFileName")
    private String attachmentFileName;

    @Transient
    private List<EmuInvoiceOrderLine> invoiceOrderLines;
}