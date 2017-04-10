package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

@Data
public class PaymentContractStep108 extends BaseEmuParser implements EmuParser {

    public static final String PAYMENTCONTRACTSTEP_108 = "PaymentContractStep-108";

    String StepId;
    String DueDate;
    String Amount;
    String AmountWithoutDec;
    String Decimals;
    String Mod10;
    String Status;
    String GrossStep;
    String NetStep;
    String VatStep;

    @Override
    public void parse(XMLStreamReader streamReader) throws XMLStreamException {
        setStepId(getField("StepId", streamReader, PAYMENTCONTRACTSTEP_108));
        setDueDate(getField("DueDate", streamReader, PAYMENTCONTRACTSTEP_108));
        setAmount(getField("Amount", streamReader, PAYMENTCONTRACTSTEP_108));
        setAmountWithoutDec(getField("AmountWithoutDec", streamReader, PAYMENTCONTRACTSTEP_108));
        setDecimals(getField("Decimals", streamReader, PAYMENTCONTRACTSTEP_108));
        setMod10(getField("Mod10", streamReader, PAYMENTCONTRACTSTEP_108));
        setStatus(getField("Status", streamReader, PAYMENTCONTRACTSTEP_108));
        setGrossStep(getField("GrossStep", streamReader, PAYMENTCONTRACTSTEP_108));
        setNetStep(getField("NetStep", streamReader, PAYMENTCONTRACTSTEP_108));
        setVatStep(getField("VatStep", streamReader, PAYMENTCONTRACTSTEP_108));
        checkEnded(PAYMENTCONTRACTSTEP_108, streamReader);
    }

    @Override
    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(PAYMENTCONTRACTSTEP_108);
        write(writer, "StepId", getStepId());
        write(writer, "DueDate", formatDate(getDueDate()));
        write(writer, "Amount", getAmount());
        write(writer, "AmountWithoutDec", getAmountWithoutDec());
        write(writer, "Decimals", getDecimals());
        write(writer, "Mod10", getMod10());
        write(writer, "Status", getStatus());
        write(writer, "GrossStep", getGrossStep());
        write(writer, "NetStep", getNetStep());
        write(writer, "VatStep", getVatStep());
        writer.writeEndElement();
    }

}