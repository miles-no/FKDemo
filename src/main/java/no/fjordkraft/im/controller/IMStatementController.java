package no.fjordkraft.im.controller;

import no.fjordkraft.im.domain.RestStatement;
import no.fjordkraft.im.model.InvoicePdf;
import no.fjordkraft.im.repository.InvoicePdfRepository;
import no.fjordkraft.im.services.impl.StatementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by miles on 6/7/2017.
 */
@Controller
@RequestMapping("/statement")
public class IMStatementController {

    @Autowired
    StatementServiceImpl statementService;

    @Autowired
    InvoicePdfRepository invoicePdfRepository;

    @RequestMapping(value = "details", method = RequestMethod.GET)
    @ResponseBody
    List<RestStatement> getDetails(@RequestParam(value = "status",required=false) String status,
                               @RequestParam(value = "fromTime", required=false) Timestamp fromTime,
                               @RequestParam(value = "toTime", required=false) Timestamp toTime,
                               @RequestParam(value = "customerID", required=false) String customerID,
                               @RequestParam(value = "brand", required=false) String brand,
                               @RequestParam(value = "page") int page,
                               @RequestParam(value = "size") int size) {
       return statementService.getDetails(page, size, status, fromTime, toTime);
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
