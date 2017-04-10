package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class ProductPackage145 extends BaseEmuParser implements EmuParser {

    public static final String PRODUCTPACKAGE_145 = "ProductPackage-145";

    String ProductId;
    String ProductSaleId;
    String Description;
    String Net;
    String Gross;
    String VAT;
    String Discount;
    String Presentation;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setProductId(getField("ProductId", streamReader, PRODUCTPACKAGE_145));
        setProductSaleId(getField("ProductSaleId", streamReader, PRODUCTPACKAGE_145));
        setDescription(getField("Description", streamReader, PRODUCTPACKAGE_145));
        setNet(getField("Net", streamReader, PRODUCTPACKAGE_145));
        setGross(getField("Gross", streamReader, PRODUCTPACKAGE_145));
        setVAT(getField("VAT", streamReader, PRODUCTPACKAGE_145));
        setDiscount(getField("Discount", streamReader, PRODUCTPACKAGE_145));
        setPresentation(getField("Presentation", streamReader, PRODUCTPACKAGE_145));
        checkEnded(PRODUCTPACKAGE_145, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer)
            throws XMLStreamException {
        writer.writeStartElement(PRODUCTPACKAGE_145);
        write(writer, "ProductId", getProductId());
        write(writer, "ProductSaleId", getProductSaleId());
        write(writer, "Description", getDescription());
        write(writer, "Net", getNet());
        write(writer, "Gross", getGross());
        write(writer, "VAT", getVAT());
        write(writer, "Discount", getDiscount());
        write(writer, "Presentation", getPresentation());
        writer.writeEndElement();
    }

}