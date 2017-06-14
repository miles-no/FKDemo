package no.fjordkraft.im.controller;

/**
 * Created by bhavik on 4/28/2017.
 */

import no.fjordkraft.im.model.InvoicePdf;
import no.fjordkraft.im.model.TransferFile;
import no.fjordkraft.im.preprocess.services.PreprocessorEngine;
import no.fjordkraft.im.preprocess.services.PreprocessorService;
import no.fjordkraft.im.repository.InvoicePdfRepository;
import no.fjordkraft.im.repository.StatementRepository;
import no.fjordkraft.im.repository.TransferFileRepository;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.Unmarshaller;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class IMController {

    private static final Logger logger = LoggerFactory.getLogger(IMController.class);

    @Autowired
    DataSource dataSource;

    @Autowired
    Unmarshaller unMarshaller;

    @Autowired
    PreprocessorEngine preprocessorEngine;

    @Autowired
    PreprocessorService preprocessorService;

    @Autowired
    TransferFileRepository transferFileRepository;

    @Autowired
    StatementRepository statementRepository;

    static int index=10;

    //static String baseDirectory = "E:\\\\FuelKraft\\\\invoice_manager\\\\if320-samples";
    static String baseDirectory = "E:\\FuelKraft\\bigfile";

    @Autowired
    InvoicePdfRepository invoicePdfRepository;


    @RequestMapping(value = "savetransferfile",method = RequestMethod.GET)
    public void saveTransferFile(@RequestParam("filepath") String filePath) throws IOException {
        logger.debug("Directory path is "+ filePath);
        File f = new File(filePath);
        String[] files = f.list();
        for(String fileName : files ) {
            TransferFile transferFile = new TransferFile();
            File xmlFile = new File(f,fileName);
            String xml = FileUtils.readFileToString(xmlFile, StandardCharsets.ISO_8859_1);

            transferFile.setId(((Integer) index++).longValue());
            transferFile.setFileName(xmlFile.getName());
            transferFile.setBrand("FKAS");
            transferFile.setFileContent(xml);
            transferFile.setTransferState("PENDING");

            transferFileRepository.save(transferFile);
        }
    }

    @RequestMapping(value = "transferfile",method = RequestMethod.GET)
    public void transferFile(@RequestParam("filepath") String filePath) throws IOException {

        File f = new File("E:\\\\FuelKraft\\\\invoice_manager\\\\if320-samples\\\\20170320_TKAS_1489996002682_statement_e2b_ehf.xml");
        String[] files = f.list();

        TransferFile transferFile = new TransferFile();
        //File xmlFile = new File(f,fileName);
        String xml = FileUtils.readFileToString(f, StandardCharsets.ISO_8859_1);

        transferFile.setId(((Integer) index++).longValue());
        transferFile.setFileName(f.getName());
        transferFile.setBrand("TKAS");
        transferFile.setFileContent(xml);
        transferFile.setTransferState("PENDING");

        transferFileRepository.save(transferFile);

    }

    @RequestMapping(value = "getinvoicepdf", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getInvoicePdf(@RequestParam("id") Long id) throws IOException {
        InvoicePdf invoicePdf = invoicePdfRepository.findOne(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(invoicePdf.getPayload().length);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=sample.pdf");
        return new ResponseEntity<>(invoicePdf.getPayload(), headers, HttpStatus.OK);
    }


}