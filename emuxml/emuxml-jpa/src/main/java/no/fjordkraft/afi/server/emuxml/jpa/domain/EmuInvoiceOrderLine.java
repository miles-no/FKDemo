package no.fjordkraft.afi.server.emuxml.jpa.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Data
@Entity
@IdClass(EmuInvoiceOrderLineId.class)
@Table(name = "emu_invoiceorderline")
public class EmuInvoiceOrderLine {

    @Id
    @Column(name = "invoiceNo")
    private Long invoiceNo;

    @Id
    @Column(name = "eioid")
    private Integer eioid;

    @Id
    @Column(name = "eiolid")
    private Integer eiolid;

    @Column(name = "startDate")
    private Date startDate;
    @Column(name = "endDate")
    private Date endDate;
    @Column(name = "kwh")
    private BigDecimal kwh;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "amount")
    private BigDecimal amount;

}