package no.fjordkraft.afi.server.emuxml.jpa.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmuInvoiceOrderLineId implements Serializable {
    private Long invoiceNo;
    private Integer eioid;
    private Integer eiolid;
}