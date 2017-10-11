package no.fjordkraft.im.controller;

import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.services.PDFGenerator;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import org.eclipse.birt.core.exception.BirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by bhavi on 9/1/2017.
 */

@RestController
public class PDFController {

    private static final Logger logger = LoggerFactory.getLogger(PDFController.class);

    @Autowired
    PDFGenerator pdfGenerator;

    @RequestMapping(value = "/pdf/{statementId}", method = RequestMethod.POST)
    public void generateInvoicePdf(@PathVariable("statementId") Long statementId){
        pdfGenerator.processStatement(statementId);
    }

    @RequestMapping(value = "/pdf", method = RequestMethod.POST)
    public void generateInvoicePdf(@RequestBody List<Long> statementIdList){

        for(Long statementId : statementIdList) {
            logger.debug("Queuing for PDF generation "+ statementId);
            pdfGenerator.processStatement(statementId);
        }

    }

    @RequestMapping(value = "/api/layout/preview", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getDesignPreview(@RequestParam("layoutId") Long layoutId,
                                                   @RequestParam("version") Integer version) throws IOException, BirtException {
        byte[] pdf = pdfGenerator.generatePreview(layoutId, version);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(pdf.length);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=sample.pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
