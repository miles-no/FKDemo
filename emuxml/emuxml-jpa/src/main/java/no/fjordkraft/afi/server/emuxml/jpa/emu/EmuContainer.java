package no.fjordkraft.afi.server.emuxml.jpa.emu;

import lombok.Data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.util.ArrayList;
import java.util.List;

@Data
public class EmuContainer<T extends EmuParser> extends ArrayList<T> implements List<T> {

    private Class<T> clazz;
    private T current;

    public EmuContainer(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void parse(XMLStreamReader emuReader) throws XMLStreamException {
        T newEl = null;
        try {
            newEl = this.clazz.newInstance();
        } catch (Exception e) {
        }
        current = newEl;
        super.add(newEl);
        current.parse(emuReader);
    }

    public void writeXml(XMLStreamWriter writer) throws XMLStreamException {
        for (T el : this) {
            el.writeXml(writer);
        }
    }
}