package no.fjordkraft.im.controller;

import no.fjordkraft.im.model.InvoicePdf;
import no.fjordkraft.im.preprocess.services.impl.UtilityService;
import no.fjordkraft.im.repository.InvoicePdfRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by bhavi on 9/14/2017.
 */

@RestController
public class PreprocessController {

    private static final Logger logger = LoggerFactory.getLogger(PreprocessController.class);

    @Autowired
    UtilityService utilityService;

    @Autowired
    InvoicePdfRepository invoicePdfRepository;

    @RequestMapping(value = "/preprocess",method= RequestMethod.POST)
    public void preprocessXml(@RequestParam("srcpath") String srcpath,@RequestParam(value = "destpath") String destPath){
        try {
            utilityService.decodeEHFE2B(srcpath,destPath);
        } catch (Exception e) {
            logger.debug("exception ",e);
        }
    }

    @RequestMapping(value = "/pdf/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable("id") Long id) throws IOException {
        InvoicePdf invoicePdf = invoicePdfRepository.findOne(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentLength(invoicePdf.getPayload().length);
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=invoice.pdf");
        return new ResponseEntity<>(invoicePdf.getPayload(), headers, HttpStatus.OK);
    }


}
