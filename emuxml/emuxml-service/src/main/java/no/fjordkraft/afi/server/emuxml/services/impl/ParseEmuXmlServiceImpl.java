package no.fjordkraft.afi.server.emuxml.services.impl;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.basis.jpa.domain.*;
import no.fjordkraft.afi.server.basis.services.AfiAccountService;
import no.fjordkraft.afi.server.basis.services.AfiCustomerService;
import no.fjordkraft.afi.server.basis.services.AfiSalesOrderAddressService;
import no.fjordkraft.afi.server.basis.services.AfiSalesOrderService;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFile;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFileStatusEnum;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuXmlHandleRequest;
import no.fjordkraft.afi.server.emuxml.jpa.emu.BaseEmuParser;
import no.fjordkraft.afi.server.emuxml.jpa.emu.Invoice;
import no.fjordkraft.afi.server.emuxml.jpa.emu.InvoiceOrder;
import no.fjordkraft.afi.server.emuxml.jpa.emu.UtilityInfo001;
import no.fjordkraft.afi.server.emuxml.services.*;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Optional;

@Slf4j
@Service("ParseEmuXmlService")
public class ParseEmuXmlServiceImpl extends BaseEmuParser implements ParseEmuXmlService {

    @Autowired
    private EmuXmlConfig emuXmlConfig;

    @Autowired
    private AfiCustomerService afiCustomerService;

    @Autowired
    private AfiAccountService afiAccountService;

    @Autowired
    private AfiSalesOrderService afiSalesOrderService;

    @Autowired
    private AfiSalesOrderAddressService afiSalesOrderAddressService;

    @Autowired
    private EmuFileUpdateService emuFileUpdateService;

    @Autowired
    private EmuXmlStoreService emuXmlStoreService;

    @Autowired
    private AttachmentStoreService attachmentStoreService;

    @Autowired
    private IF318StoreService if318StoreService;

    public ParseEmuXmlServiceImpl() {
    }

    public ParseEmuXmlServiceImpl(AttachmentStoreService attachmentStoreService,
                                  IF318StoreService if318StoreService) {
        this.attachmentStoreService = attachmentStoreService;
        this.if318StoreService = if318StoreService;
    }

    @Transactional
    @Override
    public void parseFile(EmuXmlHandleRequest request, String emuFromPath, String toAttachmentPath, String to318Path) {
        String fromFile = createEmuXmlPath(request, emuFromPath);
        EmuFile emuFile = null;
        try {
            Stopwatch sw = Stopwatch.createStarted();
            log.info(String.format("parseFile: EMU-file: start: %s",
                    fromFile));
            emuFile = emuFileUpdateService.create(request);
            long numTransactions = countNumberOfTransactions(fromFile);
            request.setNumTransactions(numTransactions);
            if (emuFile != null) {
                emuFile.setNumberOfInvoices(numTransactions);
            }
            XMLStreamReader emuReader = createEmuReader(fromFile, XMLInputFactory.newInstance());
            XMLStreamWriter if318Writer = if318StoreService.createIF318Writer(request, to318Path);
            while (emuReader.hasNext()) {
                emuReader.next();
                if (emuReader.getEventType() == XMLStreamReader.START_ELEMENT &&
                        UtilityInfo001.UTILITYINFO_001.equals(emuReader.getLocalName())) {
                    parseUtilityInfo(emuFile, emuReader);
                }
                if (emuReader.getEventType() == XMLStreamReader.START_ELEMENT &&
                        Invoice.INVOICE.equals(emuReader.getLocalName())) {
                    parseInvoice(request, emuFile, new Invoice(), emuReader, if318Writer, toAttachmentPath);
                }
            }
            if318StoreService.closeFile(if318Writer);

            if (emuFile != null) {
                emuFile.setStatus(EmuFileStatusEnum.ok);
            }

            log.info(String.format("parseFile: EMU-file: finished: %s, invoices: %d  (%s)",
                    fromFile,
                    numTransactions,
                    sw.stop().toString()));
        } catch (XMLStreamException | IOException e) {
            if (emuFile != null) {
                emuFile.setStatus(EmuFileStatusEnum.error);
            }
            log.error(String.format("parseFile: EMU-file: error: %s  (%s)",
                    fromFile, e.getMessage()));
        } finally {
            if (emuFile != null) {
                emuFile.setScanned(new Timestamp(System.currentTimeMillis()));
                emuFileUpdateService.update(emuFile);
            }
        }
    }

    @Override
    public void parseFile(EmuXmlHandleRequest request) {
        parseFile(request,
                emuXmlConfig.getScanDirectory(),
                emuXmlConfig.getAttachDestinationDirectory(),
                emuXmlConfig.getIF318DestinationDirectory());
    }

    private String createEmuXmlPath(EmuXmlHandleRequest request, String fromPath) {
        return (fromPath != null ? fromPath : "") + File.separator + request.getEmuXmlFilename();
    }

    private long countNumberOfTransactions(String fromFile) {
        long numTransactions = 0;
        try {
            Stopwatch sw = Stopwatch.createStarted();
            XMLStreamReader emuReader = createEmuReader(fromFile, XMLInputFactory.newInstance());
            while (emuReader.hasNext()) {
                emuReader.next();
                if (emuReader.getEventType() == XMLStreamReader.START_ELEMENT &&
                        Invoice.INVOICE.equals(emuReader.getLocalName())) {
                    numTransactions++;
                }
            }
            log.debug(String.format("countNumberOfTransactions: %d transactions  (%s)",
                    numTransactions,
                    sw.stop().toString()));
        } catch (XMLStreamException | FileNotFoundException e) {
            String msg = String.format("Error counting number of Invoices in file: %s", fromFile);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
        return numTransactions;
    }

    private XMLStreamReader createEmuReader(String filename, XMLInputFactory factory)
            throws XMLStreamException, FileNotFoundException {
        return factory.createXMLStreamReader(
                new InputStreamReader(new FileInputStream(filename), StandardCharsets.ISO_8859_1));
    }

    private void parseUtilityInfo(EmuFile emuFile, XMLStreamReader emuReader)
            throws XMLStreamException {
        if (emuFile != null) {
            UtilityInfo001 utilityInfo001 = new UtilityInfo001();
            utilityInfo001.parse(emuReader);
            DateTime productionDate = utilityInfo001.getProductionDate() != null ?
                    DateTimeFormat.forPattern("dd.MM.YYYY").parseDateTime(utilityInfo001.getProductionDate()) :
                    null;
            emuFile.setProductionDate(productionDate != null ? new Date(productionDate.getMillis()) : null);
        }
    }

    private void parseInvoice(EmuXmlHandleRequest request, EmuFile emuFile,
                              Invoice invoice,
                              XMLStreamReader emuReader, XMLStreamWriter if318Writer,
                              String toPath)
            throws XMLStreamException {
        while (emuReader.hasNext()) {
            emuReader.next();
            if (emuReader.getEventType() == XMLStreamReader.START_ELEMENT) {
                invoice.parse(emuReader);
            } else if (emuReader.getEventType() == XMLStreamReader.END_ELEMENT &&
                    Invoice.INVOICE.equals(emuReader.getLocalName())) {
                fetchAfiSalesorders(invoice);
                findAccount(invoice);
                String attachmentFileName = attachmentStoreService.createAttachmentFileName(invoice);
                if (attachmentFileName != null && request.isStoreAttachmentFile()) {
                    attachmentStoreService.storeAttachmentXml(invoice, attachmentFileName, toPath);
                }
                if (request.isStoreIF318File()) {
                    if318StoreService.store318Xml(invoice, if318Writer, attachmentFileName);
                }
                if (emuFile != null) {
                    emuFile.addGiroAmount(new BigDecimal(invoice.getMainInvoiceInfo101().getCurrent().getToBePaid()));
                    emuFile.addPrintedAmount(new BigDecimal(invoice.getMainInvoiceInfo101().getCurrent().getPrintedAmount()));
                }
                emuXmlStoreService.store(emuFile, invoice, attachmentFileName);
                break;
            }
        }
    }

    private void fetchAfiSalesorders(Invoice invoice) {
        for (InvoiceOrder invoiceOrder : invoice.getInvoiceOrder()) {
            if (shallFetchAfiSalesOrders(invoiceOrder)) {
                Page<AfiSalesOrder> afiSalesOrders = afiSalesOrderService.list(
                        SalesOrderFetchRequest.forExternalinvoiceaccountidAndDeliverypointId(
                                invoice.getInvoiceAgreementInfo105().getCurrent().getInvAgreementId(),
                                invoiceOrder.getSupplyPointInfo117().getCurrent().getObjectId()));
                if (afiSalesOrders.getContent().size() > 0) {
                    Optional<AfiSalesOrder> afiSalesOrder = afiSalesOrders.getContent().stream()
                            .sorted((so1, so2) -> Long.compare(so2.getFromDate().getTime(), so1.getFromDate().getTime()))
                            .findFirst();
                    if (afiSalesOrder.isPresent()) {
                        invoiceOrder.setAfiSalesOrder(afiSalesOrder.get());
                        invoiceOrder.setAfiSalesOrderAddress(afiSalesOrderAddressService.get(invoiceOrder.getAfiSalesOrder().getSalesOrderId()));
                    }
                }
            }
        }
    }

    private boolean shallFetchAfiSalesOrders(InvoiceOrder invoiceOrder) {
        return invoiceOrder.getSupplyPointInfo117().getCurrent() != null &&
                StringUtils.isNotEmpty(invoiceOrder.getSupplyPointInfo117().getCurrent().getObjectId());
    }

    private void findAccount(Invoice invoice) {
        String invAgreementId = invoice.getInvoiceAgreementInfo105().getCurrent().getInvAgreementId();
        AccountFetchRequest request = AccountFetchRequest.forExternalInvoiceAccountId(invAgreementId);
        for (InvoiceOrder invoiceOrder : invoice.getInvoiceOrder()) {
            if (invoiceOrder.getAfiSalesOrder() != null) {
                request = AccountFetchRequest.one(invoiceOrder.getAfiSalesOrder().getAccountNo());
                break;
            }
        }
        AfiAccount afiAccount = null;
        try {
            afiAccount = afiAccountService.get(request);
        } catch (Exception e) {
            log.error(String.format("Error fetching account: request: %s  (%s)",
                    request.toString(),
                    e.getMessage()));
        }
        if (afiAccount != null) {
            invoice.setBrand(afiAccount.getBrand());
            invoice.setAccountNo(afiAccount.getAccountNo());
            AfiCustomer afiCustomer = afiCustomerService.get(afiAccount.getCustomerId());
            invoice.setCustomerType(afiCustomer != null ? afiCustomer.getType() : null);
        } else {
            log.error(String.format("Unable to find account for invoice agreement ID: %s", invAgreementId));
            invoice.setBrand("NOBRAND");
            invoice.setAccountNo("ACCOUNT_NO");
        }
    }

}
