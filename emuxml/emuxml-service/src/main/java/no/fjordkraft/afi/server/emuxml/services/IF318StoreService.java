package no.fjordkraft.afi.server.emuxml.services;

import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuXmlHandleRequest;
import no.fjordkraft.afi.server.emuxml.jpa.emu.Invoice;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;

public interface IF318StoreService {

    /**
     * Create IF318 XmlStreamWriter.
     *
     * @param request
     * @param to318Path
     * @return
     * @throws IOException
     */
    XMLStreamWriter createIF318Writer(EmuXmlHandleRequest request, String to318Path)
            throws IOException;

    /**
     * Creates header for the IF318 file.
     *
     * @param filename
     * @param numTransactions
     * @return
     */
    XMLStreamWriter createFile(String filename, long numTransactions);

    /**
     * Stores IF318 file from EMU-XML - file.
     *
     * @param invoice
     * @param writer
     * @param specificationFile
     * @throws XMLStreamException
     */
    void store318Xml(Invoice invoice, XMLStreamWriter writer, String specificationFile)
            throws XMLStreamException;

    /**
     * Creates footer and closes the IF318 file.
     *
     * @param writer
     */
    void closeFile(XMLStreamWriter writer);

}
