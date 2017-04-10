package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.basis.jpa.domain.ShortCustomerTypeEnum;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.math.BigDecimal;

@Slf4j
@Data
public class Invoice extends BaseEmuParser implements EmuParser {

    public static final String INVOICE = "Invoice";

    private String brand;
    private String accountNo;
    private ShortCustomerTypeEnum customerType;

    private EmuContainer<MainInvoiceInfo101> mainInvoiceInfo101 = new EmuContainer<>(MainInvoiceInfo101.class);
    private EmuContainer<DunningStepInfo102> dunningStepInfo102 = new EmuContainer<>(DunningStepInfo102.class);
    private EmuContainer<CustomerInfo103> customerInfo103 = new EmuContainer<>(CustomerInfo103.class);
    private EmuContainer<UnpaidInvoiceInfo104> unpaidInvoiceInfo104 = new EmuContainer<>(UnpaidInvoiceInfo104.class);
    private EmuContainer<InvoiceAgreementInfo105> invoiceAgreementInfo105 = new EmuContainer<>(InvoiceAgreementInfo105.class);
    private EmuContainer<InvoiceDunningInfo106> invoiceDunningInfo106 = new EmuContainer<>(InvoiceDunningInfo106.class);
    private EmuContainer<PaymentContract107> paymentContract107 = new EmuContainer<>(PaymentContract107.class);
    private EmuContainer<PaymentContractStep108> paymentContractStep108 = new EmuContainer<>(PaymentContractStep108.class);
    private EmuContainer<PassThroughInvoiceInfo109> passThroughInvoiceInfo109 = new EmuContainer<>(PassThroughInvoiceInfo109.class);
    private EmuContainer<InvoiceOrder> invoiceOrder = new EmuContainer<>(InvoiceOrder.class);
    private EmuContainer<GenInfoInvoiceAgreement140> genInfoInvoiceAgreement140 = new EmuContainer<>(GenInfoInvoiceAgreement140.class);
    private EmuContainer<PayerInfo141> payerInfo141 = new EmuContainer<>(PayerInfo141.class);
    private EmuContainer<VatSpecInvoice142> vatSpecInvoice142 = new EmuContainer<>(VatSpecInvoice142.class);
    private EmuContainer<UrlTouch143> urlTouch143 = new EmuContainer<>(UrlTouch143.class);
    private EmuContainer<Attachment144> attachment144 = new EmuContainer<>(Attachment144.class);
    private EmuContainer<ProductPackage145> productPackage145 = new EmuContainer<>(ProductPackage145.class);
    private EmuContainer<DunningCharge146> dunningCharge146 = new EmuContainer<>(DunningCharge146.class);
    private EmuContainer<ActivityOverview147> activityOverview147 = new EmuContainer<>(ActivityOverview147.class);
    private EmuContainer<ExternalAddressOverview148> externalAddressOverview148 = new EmuContainer<>(ExternalAddressOverview148.class);
    private EmuContainer<InvoiceEndRecord199> invoiceEndRecord199 = new EmuContainer<>(InvoiceEndRecord199.class);

    public Long getInvoiceNo() {
        Long ret = null;
        try {
            ret = Long.parseLong(mainInvoiceInfo101.getCurrent().getInvoiceNo());
        } catch (NumberFormatException e) {
            log.error(String.format("Error parsing invoice No: %s", getMainInvoiceInfo101().getCurrent().getInvoiceNo()), e);
        }
        return ret;
    }

    public InvoiceOrder getFirstInvoiceOrder() {
        return invoiceOrder.size() > 0 ? invoiceOrder.get(0) : null;
    }

    public String getTypeOfChange() {
        BigDecimal amountExclusiveOfVat = new BigDecimal(getMainInvoiceInfo101().getCurrent().getNetPrintet());
        String invoiceType = null;
        InvoiceOrder io = getFirstInvoiceOrder();
        if (io != null && io.getInvoiceOrderInfo110().getCurrent() != null) {
            invoiceType = io.getInvoiceOrderInfo110().getCurrent().getInvoiceType();
        }

        if (invoiceType == null || "K".equals(invoiceType)) {
            if (amountExclusiveOfVat.compareTo(BigDecimal.ZERO) <= 0) {
                return "FCREDITINV";
            } else {
                return "FPCREDINV";
            }
        }
        if (amountExclusiveOfVat.compareTo(BigDecimal.ZERO) >= 0) {
            return "FINVOICE";
        } else {
            return "FNINVOICE";
        }
    }

    public String getVatTypeOfCharge() {
        BigDecimal vat = new BigDecimal(getMainInvoiceInfo101().getCurrent().getVAT());
        if (vat.compareTo(BigDecimal.ZERO) >= 0) {
            return "VAT";
        } else {
            return "VATCREDIT";
        }
    }

    /**
     * Return TRUE if all invoiceOrder.invoiceType == 'K' for an invoice.
     *
     * @return
     */
    public boolean getAllCreditInvoice() {
        String invoiceType = null;
        InvoiceOrder io = getFirstInvoiceOrder();
        if (io != null && io.getInvoiceOrderInfo110().getCurrent() != null) {
            invoiceType = io.getInvoiceOrderInfo110().getCurrent().getInvoiceType();
        }
        return invoiceType == null || "K".equals(invoiceType);
    }

    @Override
    public void parse(XMLStreamReader emuReader) throws XMLStreamException {
        switch (emuReader.getLocalName()) {
            case MainInvoiceInfo101.MAIN_INVOICE_INFO_101:
                mainInvoiceInfo101.parse(emuReader);
                break;
            case DunningStepInfo102.DUNNINGSTEPINFO_102:
                dunningStepInfo102.parse(emuReader);
                break;
            case CustomerInfo103.CUSTOMERINFO_103:
                customerInfo103.parse(emuReader);
                break;
            case UnpaidInvoiceInfo104.UNPAIDINVOICEINFO_104:
                unpaidInvoiceInfo104.parse(emuReader);
                break;
            case InvoiceAgreementInfo105.INVOICEAGREEMENTINFO_105:
                invoiceAgreementInfo105.parse(emuReader);
                break;
            case InvoiceDunningInfo106.INVOICEDUNNINGINFO_106:
                invoiceDunningInfo106.parse(emuReader);
                break;
            case PaymentContract107.PAYMENTCONTRACT_107:
                paymentContract107.parse(emuReader);
                break;
            case PaymentContractStep108.PAYMENTCONTRACTSTEP_108:
                paymentContractStep108.parse(emuReader);
                break;
            case PassThroughInvoiceInfo109.PASS_THROUGHINVOICEINFO_109:
                passThroughInvoiceInfo109.parse(emuReader);
                break;
            case InvoiceOrder.INVOICE_ORDER:
                invoiceOrder.parse(emuReader);
                break;
            case GenInfoInvoiceAgreement140.GENINFOINVOICEAGREEMENT_140:
                genInfoInvoiceAgreement140.parse(emuReader);
                break;
            case PayerInfo141.PAYERINFO_141:
                payerInfo141.parse(emuReader);
                break;
            case VatSpecInvoice142.VATSPECINVOICE_142:
                vatSpecInvoice142.parse(emuReader);
                break;
            case UrlTouch143.URL_TOUCH_143:
                urlTouch143.parse(emuReader);
                break;
            case Attachment144.ATTACHMENT_144:
                attachment144.parse(emuReader);
                break;
            case ProductPackage145.PRODUCTPACKAGE_145:
                productPackage145.parse(emuReader);
                break;
            case DunningCharge146.DUNNINGCHARGE_146:
                dunningCharge146.parse(emuReader);
                break;
            case ActivityOverview147.ACTIVITYOVERVIEW_147:
                activityOverview147.parse(emuReader);
                break;
            case ExternalAddressOverview148.EXTERNALADDRESSOVERVIEW_148:
                externalAddressOverview148.parse(emuReader);
                break;
            case InvoiceEndRecord199.INVOICEENDRECORD_199:
                invoiceEndRecord199.parse(emuReader);
                break;
        }
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("FAKTURA");
        write(writer, "AKTOR", "TKAS".equals(brand) ? "50153834" : "50153687");
        writeCData(writer, "AKTORNAVN", "TKAS".equals(brand) ? "Trondheim Kraft AS" : "Fjordkraft AS");
        write(writer, "FAKTURANR", "" + getInvoiceNo());
        write(writer, "MAALEPUNKT", getMaalepunktId());
        write(writer, "FAKTURA_TYPE", "FAKTURA");
        write(writer, "KUNDE_TYPE", "P".equals(getInvoiceAgreementInfo105().getCurrent().getIndustryPrivate()) ? "P" : "B");
        write(writer, "FAKTURA_MERKE_SO", StringUtils.isNotEmpty(getInvoiceAgreementInfo105().getCurrent().getInvoiceLabel()) ?
                getInvoiceAgreementInfo105().getCurrent().getInvoiceLabel() : "");
        write(writer, "VEDLEGG_FORMAT", "EMUXML");

        writer.writeStartElement("VEDLEGG_EMUXML");
        writer.writeStartElement("Invoice");
        mainInvoiceInfo101.writeXml(writer);
        dunningStepInfo102.writeXml(writer);
        customerInfo103.writeXml(writer);
        unpaidInvoiceInfo104.writeXml(writer);
        invoiceAgreementInfo105.writeXml(writer);
        invoiceDunningInfo106.writeXml(writer);
        paymentContract107.writeXml(writer);
        paymentContractStep108.writeXml(writer);
        passThroughInvoiceInfo109.writeXml(writer);
        invoiceOrder.writeXml(writer);
        genInfoInvoiceAgreement140.writeXml(writer);
        payerInfo141.writeXml(writer);
        vatSpecInvoice142.writeXml(writer);
        urlTouch143.writeXml(writer);
        attachment144.writeXml(writer);
        productPackage145.writeXml(writer);
        dunningCharge146.writeXml(writer);
        activityOverview147.writeXml(writer);
        externalAddressOverview148.writeXml(writer);
        invoiceEndRecord199.writeXml(writer);
        writer.writeEndElement();
        writer.writeEndElement();

        writer.writeStartElement("VEDLEGG_E2B");
        writer.writeCharacters("\n\n");
        writer.writeEndElement();
        writer.writeEndElement();
    }

    private String getMaalepunktId() {
        if (invoiceOrder.size() > 0) {
            for (int i = invoiceOrder.size() - 1; i >= 0; i--) {
                InvoiceOrder io = invoiceOrder.get(i);
                if (io.getSupplyPointInfo117() != null) {
                    SupplyPointInfo117 spi = io.getSupplyPointInfo117().getCurrent();
                    if (spi != null && StringUtils.isNotEmpty(spi.getObjectId())) {
                        return spi.getObjectId();
                    }
                }
            }
        }
        return "";
    }
}
