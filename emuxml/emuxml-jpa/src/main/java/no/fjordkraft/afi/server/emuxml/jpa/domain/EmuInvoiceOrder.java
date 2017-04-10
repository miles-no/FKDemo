package no.fjordkraft.afi.server.emuxml.jpa.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@IdClass(EmuInvoiceOrderId.class)
@Table(name = "emu_invoiceorder")
public class EmuInvoiceOrder {

    @Id
    @Column(name = "invoiceNo")
    private Long invoiceNo;

    @Id
    @Column(name = "eioid")
    private Integer eioid;

    @Column(name = "deliverypointid")
    private String deliveryPointId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "closeCode")
    private String closeCode;

    @Column(name = "invoiceType")
    private String invoiceType;

}