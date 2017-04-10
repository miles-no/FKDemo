package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.basis.jpa.domain.AfiSalesOrder;
import no.fjordkraft.afi.server.basis.jpa.domain.AfiSalesOrderAddress;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Slf4j
@Data
public class InvoiceOrder extends BaseEmuParser implements EmuParser {

    public static final String INVOICE_ORDER = "InvoiceOrder";

    private AfiSalesOrder afiSalesOrder;
    private AfiSalesOrderAddress afiSalesOrderAddress;

    private EmuContainer<InvoiceOrderInfo110> invoiceOrderInfo110 = new EmuContainer<>(InvoiceOrderInfo110.class);
    private EmuContainer<ReadingInfo111> readingInfo111 = new EmuContainer<>(ReadingInfo111.class);
    private EmuContainer<LastReading112> lastReading112 = new EmuContainer<>(LastReading112.class);
    private EmuContainer<InvoiceOrderAmounts113> invoiceOrderAmounts113 = new EmuContainer<>(InvoiceOrderAmounts113.class);
    private EmuContainer<VatSpecInvoiceOrder114> vatSpecInvoiceOrder114 = new EmuContainer<>(VatSpecInvoiceOrder114.class);
    private EmuContainer<PriceInfo116> priceInfo116 = new EmuContainer<>(PriceInfo116.class);
    private EmuContainer<SupplyPointInfo117> supplyPointInfo117 = new EmuContainer<>(SupplyPointInfo117.class);
    private EmuContainer<ProductParameters118> productParameters118 = new EmuContainer<>(ProductParameters118.class);
    private EmuContainer<InvoiceLine120> invoiceLine120 = new EmuContainer<>(InvoiceLine120.class);
    private EmuContainer<ExternalAddress121> externalAddress121 = new EmuContainer<>(ExternalAddress121.class);
    private EmuContainer<MoraSpecification122> moraSpecification122 = new EmuContainer<>(MoraSpecification122.class);
    private EmuContainer<YearlyConsumption123> yearlyConsumption123 = new EmuContainer<>(YearlyConsumption123.class);
    private EmuContainer<InvoiceText124> invoiceText124 = new EmuContainer<>(InvoiceText124.class);
    private EmuContainer<GenInfoInvoiceOrder125> genInfoInvoiceOrder125 = new EmuContainer<>(GenInfoInvoiceOrder125.class);
    private EmuContainer<HistoricalInfo130> historicalInfo130 = new EmuContainer<>(HistoricalInfo130.class);
    private EmuContainer<ConsumptionPillars132> consumptionPillars132 = new EmuContainer<>(ConsumptionPillars132.class);
    private EmuContainer<DeliveryAddress133> deliveryAddress133 = new EmuContainer<>(DeliveryAddress133.class);
    private EmuContainer<InvoiceOrderEndRecord134> invoiceOrderEndRecord134 = new EmuContainer<>(InvoiceOrderEndRecord134.class);

    public InvoiceOrder() {
    }

    public String getShortAddress() {
        String address = getAfiSalesOrderAddress() != null && getAfiSalesOrderAddress().getAddress() != null ?
                getAfiSalesOrderAddress().getAddress() :
                "";
        return WordUtils.capitalizeFully(address);
    }

    public String getAddress() {
        String address = getAfiSalesOrderAddress() != null ?
                (StringUtils.isNotEmpty(getAfiSalesOrderAddress().getAddress()) ? getAfiSalesOrderAddress().getAddress() + " " : "") +
                        (StringUtils.isNotEmpty(getAfiSalesOrderAddress().getAddress_2()) ? getAfiSalesOrderAddress().getAddress_2() + " " : "") +
                        (StringUtils.isNotEmpty(getAfiSalesOrderAddress().getPostalCode()) ? getAfiSalesOrderAddress().getPostalCode() + " " : "") +
                        (StringUtils.isNotEmpty(getAfiSalesOrderAddress().getCity()) ? getAfiSalesOrderAddress().getCity() : "") :
                "";
        return WordUtils.capitalizeFully(address);
    }

    public boolean shallAddDistribution() {
        return getSupplyPointInfo117().getCurrent() != null &&
                StringUtils.isNotEmpty(getSupplyPointInfo117().getCurrent().getObjectId());
    }

    public String getDisconnected() {
        String invoiceType = getInvoiceOrderInfo110().getCurrent().getInvoiceType();
        String closeCode = getInvoiceOrderInfo110().getCurrent().getCloseCode();
        if ("B".equals(invoiceType) && "U".equals(closeCode)) {
            return "0";
        }
        if ("B".equals(invoiceType) && "K".equals(closeCode)) {
            return "0";
        }
        if ("K".equals(invoiceType)) {
            return "0";
        }
        return StringUtils.isEmpty(getInvoiceOrderInfo110().getCurrent().getCloseCode()) ? "0" : "1";
    }

    @Override
    public void parse(XMLStreamReader emuReader) throws XMLStreamException {
        while (emuReader.hasNext()) {
            emuReader.next();
            if (emuReader.getEventType() == XMLStreamReader.START_ELEMENT) {
                switch (emuReader.getLocalName()) {
                    case InvoiceOrderInfo110.INVOICEORDERINFO_110:
                        invoiceOrderInfo110.parse(emuReader);
                        break;
                    case ReadingInfo111.READINGINFO_111:
                        readingInfo111.parse(emuReader);
                        break;
                    case LastReading112.LASTREADING_112:
                        lastReading112.parse(emuReader);
                        break;
                    case InvoiceOrderAmounts113.INVOICEORDERAMOUNTS_113:
                        invoiceOrderAmounts113.parse(emuReader);
                        break;
                    case VatSpecInvoiceOrder114.VATSPECINVOICEORDER_114:
                        vatSpecInvoiceOrder114.parse(emuReader);
                        break;
                    case PriceInfo116.PRICEINFO_116:
                        priceInfo116.parse(emuReader);
                        break;
                    case SupplyPointInfo117.SUPPLYPOINTINFO_117:
                        supplyPointInfo117.parse(emuReader);
                        break;
                    case ProductParameters118.PRODUCTPARAMETERS_118:
                        productParameters118.parse(emuReader);
                        break;
                    case InvoiceLine120.INVOICELINE_120:
                        invoiceLine120.parse(emuReader);
                        break;
                    case ExternalAddress121.EXTERNALADDRESS_121:
                        externalAddress121.parse(emuReader);
                        break;
                    case MoraSpecification122.MORASPECIFICATION_122:
                        moraSpecification122.parse(emuReader);
                        break;
                    case YearlyConsumption123.YEARLYCONSUMPTION_123:
                        yearlyConsumption123.parse(emuReader);
                        break;
                    case InvoiceText124.INVOICETEXT_124:
                        invoiceText124.parse(emuReader);
                        break;
                    case GenInfoInvoiceOrder125.GENINFOINVOICEORDER_125:
                        genInfoInvoiceOrder125.parse(emuReader);
                        break;
                    case HistoricalInfo130.HISTORICALINFO_130:
                        historicalInfo130.parse(emuReader);
                        break;
                    case ConsumptionPillars132.CONSUMPTIONPILLARS_132:
                        consumptionPillars132.parse(emuReader);
                        break;
                    case DeliveryAddress133.DELIVERYADDRESS_133:
                        deliveryAddress133.parse(emuReader);
                        break;
                    case InvoiceOrderEndRecord134.INVOICEORDERENDRECORD_134:
                        invoiceOrderEndRecord134.parse(emuReader);
                        break;
                }
            } else if (emuReader.getEventType() == XMLStreamReader.END_ELEMENT &&
                    INVOICE_ORDER.equals(emuReader.getLocalName())) {
                break;
            }
        }
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(INVOICE_ORDER);
        invoiceOrderInfo110.writeXml(writer);
        readingInfo111.writeXml(writer);
        lastReading112.writeXml(writer);
        invoiceOrderAmounts113.writeXml(writer);
        vatSpecInvoiceOrder114.writeXml(writer);
        priceInfo116.writeXml(writer);
        supplyPointInfo117.writeXml(writer);
        productParameters118.writeXml(writer);
        invoiceLine120.writeXml(writer);
        externalAddress121.writeXml(writer);
        moraSpecification122.writeXml(writer);
        yearlyConsumption123.writeXml(writer);
        invoiceText124.writeXml(writer);
        genInfoInvoiceOrder125.writeXml(writer);
        historicalInfo130.writeXml(writer);
        consumptionPillars132.writeXml(writer);
        deliveryAddress133.writeXml(writer);
        invoiceOrderEndRecord134.writeXml(writer);
        writer.writeEndElement();
    }
}
