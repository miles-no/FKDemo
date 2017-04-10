package no.fjordkraft.afi.server.emuxml.services.impl;

import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFile;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoice;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoiceOrder;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoiceOrderLine;
import no.fjordkraft.afi.server.emuxml.jpa.emu.BaseEmuParser;
import no.fjordkraft.afi.server.emuxml.jpa.emu.Invoice;
import no.fjordkraft.afi.server.emuxml.jpa.emu.InvoiceLine120;
import no.fjordkraft.afi.server.emuxml.jpa.emu.InvoiceOrder;
import no.fjordkraft.afi.server.emuxml.jpa.repository.*;
import no.fjordkraft.afi.server.emuxml.services.EmuXmlStoreService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Slf4j
@Service("EmuXmlStoreService")
public class EmuXmlStoreServiceImpl extends BaseEmuParser implements EmuXmlStoreService {

    @Resource
    private EmuFileRepository emuFileRepository;

    @Resource
    private EmuInvoiceRepository emuInvoiceRepository;

    @Resource
    private EmuInvoiceOrderRepository emuInvoiceOrderRepository;

    @Resource
    private EmuInvoiceOrderLineRepository emuInvoiceOrderLineRepository;

    @Resource
    private EmuInvoiceSpecRepository emuInvoiceSpecRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void store(EmuFile emuFile, Invoice invoice, String attachmentFileName) {
        if (emuFile == null) {
            return;
        }

        emuInvoiceSpecRepository.deleteEmuInvoice(invoice.getInvoiceNo());

        EmuInvoice emuInvoice = new EmuInvoice();
        emuInvoice.setInvoiceNo(invoice.getInvoiceNo());
        emuInvoice.setEf_id(emuFile.getId());
        emuInvoice.setCustomerId(invoice.getMainInvoiceInfo101().getCurrent().getCustomerId());
        emuInvoice.setBrand(invoice.getBrand());
        emuInvoice.setAccountNo(invoice.getAccountNo());
        emuInvoice.setInvAgreementId(invoice.getInvoiceAgreementInfo105().getCurrent().getInvAgreementId());
        emuInvoice.setPrintDate(formatAsDate(invoice.getMainInvoiceInfo101().getCurrent().getPrintDate(), "dd.MM.yyyy"));
        emuInvoice.setGiroAmount(new BigDecimal(invoice.getMainInvoiceInfo101().getCurrent().getToBePaid()));
        emuInvoice.setPrintedAmount(new BigDecimal(invoice.getMainInvoiceInfo101().getCurrent().getPrintedAmount()));
        emuInvoice.setAttachmentFileName(attachmentFileName);
        emuInvoiceRepository.saveAndFlush(emuInvoice);

        int eioid = 1;
        for (InvoiceOrder io : invoice.getInvoiceOrder()) {
            store(invoice.getInvoiceNo(), eioid++, io);
        }
    }

    private EmuInvoiceOrder store(Long invoiceNo, int eioid, InvoiceOrder io) {
        EmuInvoiceOrder eio = new EmuInvoiceOrder();
        eio.setInvoiceNo(invoiceNo);
        eio.setEioid(eioid);
        if (io.getSupplyPointInfo117().getCurrent() != null &&
                StringUtils.isNotEmpty(io.getSupplyPointInfo117().getCurrent().getObjectId())) {
            eio.setDeliveryPointId(io.getSupplyPointInfo117().getCurrent().getObjectId());
        }
        eio.setAmount(new BigDecimal(io.getInvoiceOrderInfo110().getCurrent().getAmount()));
        eio.setCloseCode(io.getInvoiceOrderInfo110().getCurrent().getCloseCode());
        eio.setInvoiceType(io.getInvoiceOrderInfo110().getCurrent().getInvoiceType());
        emuInvoiceOrderRepository.saveAndFlush(eio);

        int eiolid = 1;
        for (InvoiceLine120 iol : io.getInvoiceLine120()) {
            store(invoiceNo, eioid, eiolid++, iol);
        }
        return eio;
    }

    private EmuInvoiceOrderLine store(Long invoiceNo, int eioid, int eiolid, InvoiceLine120 iol) {
        EmuInvoiceOrderLine eiol = new EmuInvoiceOrderLine();
        eiol.setInvoiceNo(invoiceNo);
        eiol.setEioid(eioid);
        eiol.setEiolid(eiolid);

        eiol.setStartDate(formatAsDate(iol.getStartDate(), "dd.MM.yyyy"));
        eiol.setEndDate(formatAsDate(iol.getEndDate(), "dd.MM.yyyy"));
        eiol.setKwh(new BigDecimal(iol.getBasis()));
        eiol.setPrice(new BigDecimal(iol.getGrossPrice()));
        eiol.setAmount(new BigDecimal(iol.getGross()));
        return emuInvoiceOrderLineRepository.saveAndFlush(eiol);
    }

    protected Date formatAsDate(String dateAsString, String df) {
        Date ret;
        try {
            ret = dateAsString != null ? new Date(new SimpleDateFormat(df).parse(dateAsString).getTime()) : null;
        } catch (ParseException e) {
            ret = null;
        }
        return ret;
    }
}
