package no.fjordkraft.im.services;

import no.fjordkraft.im.model.SystemBatchInput;

import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by miles on 5/2/2017.
 */
public interface StatementSplitter {
    //void batchFileSplit(InputStream inputStream, String destinationPath, Long si_id) throws XMLStreamException, IOException;

    void batchFileSplit(InputStream inputStream, String destinationPath, SystemBatchInput systemBatchInput) throws Exception;


}
