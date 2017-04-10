package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class ProductParameters118 extends BaseEmuParser implements EmuParser {

    public static final String PRODUCTPARAMETERS_118 = "ProductParameters-118";

    String ProdId;
    String Field1;
    String Field2;
    String Field3;
    String Field4;
    String Field5;
    String Field6;
    String Field7;
    String Field8;
    String Field9;
    String Field10;
    String Field11;
    String Field12;
    String Field13;
    String Field14;
    String Field15;
    String Field16;
    String Field17;
    String Field18;
    String Field19;
    String Field20;
    String Label1;
    String Label2;
    String Label3;
    String Label4;
    String Label5;
    String Label6;
    String Label7;
    String Label8;
    String Label9;
    String Label10;
    String Label11;
    String Label12;
    String Label13;
    String Label14;
    String Label15;
    String Label16;
    String Label17;
    String Label18;
    String Label19;
    String Label20;
    String Description;
    String ServiceAgreeSerialNo;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setProdId(getField("ProdId", streamReader, PRODUCTPARAMETERS_118));
        setField1(getField("Field1", streamReader, PRODUCTPARAMETERS_118));
        setField2(getField("Field2", streamReader, PRODUCTPARAMETERS_118));
        setField3(getField("Field3", streamReader, PRODUCTPARAMETERS_118));
        setField4(getField("Field4", streamReader, PRODUCTPARAMETERS_118));
        setField5(getField("Field5", streamReader, PRODUCTPARAMETERS_118));
        setField6(getField("Field6", streamReader, PRODUCTPARAMETERS_118));
        setField7(getField("Field7", streamReader, PRODUCTPARAMETERS_118));
        setField8(getField("Field8", streamReader, PRODUCTPARAMETERS_118));
        setField9(getField("Field9", streamReader, PRODUCTPARAMETERS_118));
        setField10(getField("Field10", streamReader, PRODUCTPARAMETERS_118));
        setField11(getField("Field11", streamReader, PRODUCTPARAMETERS_118));
        setField12(getField("Field12", streamReader, PRODUCTPARAMETERS_118));
        setField13(getField("Field13", streamReader, PRODUCTPARAMETERS_118));
        setField14(getField("Field14", streamReader, PRODUCTPARAMETERS_118));
        setField15(getField("Field15", streamReader, PRODUCTPARAMETERS_118));
        setField16(getField("Field16", streamReader, PRODUCTPARAMETERS_118));
        setField17(getField("Field17", streamReader, PRODUCTPARAMETERS_118));
        setField18(getField("Field18", streamReader, PRODUCTPARAMETERS_118));
        setField19(getField("Field19", streamReader, PRODUCTPARAMETERS_118));
        setField20(getField("Field20", streamReader, PRODUCTPARAMETERS_118));
        setLabel1(getField("Label1", streamReader, PRODUCTPARAMETERS_118));
        setLabel2(getField("Label2", streamReader, PRODUCTPARAMETERS_118));
        setLabel3(getField("Label3", streamReader, PRODUCTPARAMETERS_118));
        setLabel4(getField("Label4", streamReader, PRODUCTPARAMETERS_118));
        setLabel5(getField("Label5", streamReader, PRODUCTPARAMETERS_118));
        setLabel6(getField("Label6", streamReader, PRODUCTPARAMETERS_118));
        setLabel7(getField("Label7", streamReader, PRODUCTPARAMETERS_118));
        setLabel8(getField("Label8", streamReader, PRODUCTPARAMETERS_118));
        setLabel9(getField("Label9", streamReader, PRODUCTPARAMETERS_118));
        setLabel10(getField("Label10", streamReader, PRODUCTPARAMETERS_118));
        setLabel11(getField("Label11", streamReader, PRODUCTPARAMETERS_118));
        setLabel12(getField("Label12", streamReader, PRODUCTPARAMETERS_118));
        setLabel13(getField("Label13", streamReader, PRODUCTPARAMETERS_118));
        setLabel14(getField("Label14", streamReader, PRODUCTPARAMETERS_118));
        setLabel15(getField("Label15", streamReader, PRODUCTPARAMETERS_118));
        setLabel16(getField("Label16", streamReader, PRODUCTPARAMETERS_118));
        setLabel17(getField("Label17", streamReader, PRODUCTPARAMETERS_118));
        setLabel18(getField("Label18", streamReader, PRODUCTPARAMETERS_118));
        setLabel19(getField("Label19", streamReader, PRODUCTPARAMETERS_118));
        setLabel20(getField("Label20", streamReader, PRODUCTPARAMETERS_118));
        setDescription(getField("Description", streamReader, PRODUCTPARAMETERS_118));
        setServiceAgreeSerialNo(getField("ServiceAgreeSerialNo", streamReader, PRODUCTPARAMETERS_118));
        checkEnded(PRODUCTPARAMETERS_118, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(PRODUCTPARAMETERS_118);
        writeCDataEx(writer, "ProdId", getProdId());
        writeCDataEx(writer, "Field1", getField1());
        writeCDataEx(writer, "Field2", getField2());
        write(writer, "Field3", getField3());
        write(writer, "Field4", getField4());
        write(writer, "Field5", getField5());
        write(writer, "Field6", getField6());
        write(writer, "Field7", getField7());
        write(writer, "Field8", getField8());
        write(writer, "Field9", getField9());
        write(writer, "Field10", getField10());
        write(writer, "Field11", getField11());
        write(writer, "Field12", getField12());
        write(writer, "Field13", getField13());
        write(writer, "Field14", getField14());
        write(writer, "Field15", getField15());
        write(writer, "Field16", getField16());
        write(writer, "Field17", getField17());
        write(writer, "Field18", getField18());
        write(writer, "Field19", getField19());
        write(writer, "Field20", getField20());
        writeCDataEx(writer, "Label1", getLabel1());
        writeCDataEx(writer, "Label2", getLabel2());
        writeCDataEx(writer, "Label3", getLabel3());
        writeCDataEx(writer, "Label4", getLabel4());
        writeCDataEx(writer, "Label5", getLabel5());
        writeCDataEx(writer, "Label6", getLabel6());
        writeCDataEx(writer, "Label7", getLabel7());
        writeCDataEx(writer, "Label8", getLabel8());
        writeCDataEx(writer, "Label9", getLabel9());
        writeCDataEx(writer, "Label10", getLabel10());
        writeCDataEx(writer, "Label11", getLabel11());
        writeCDataEx(writer, "Label12", getLabel12());
        writeCDataEx(writer, "Label13", getLabel13());
        writeCDataEx(writer, "Label14", getLabel14());
        writeCDataEx(writer, "Label15", getLabel15());
        writeCDataEx(writer, "Label16", getLabel16());
        writeCDataEx(writer, "Label17", getLabel17());
        writeCDataEx(writer, "Label18", getLabel18());
        writeCDataEx(writer, "Label19", getLabel19());
        writeCDataEx(writer, "Label20", getLabel20());
        writeCDataEx(writer, "Description", getDescription());
        write(writer, "ServiceAgreeSerialNo", getServiceAgreeSerialNo());
        writer.writeEndElement();
    }
}
