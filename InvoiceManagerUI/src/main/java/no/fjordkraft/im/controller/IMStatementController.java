package no.fjordkraft.im.controller;

import no.fjordkraft.im.domain.RestStatement;
import no.fjordkraft.im.model.InvoicePdf;
import no.fjordkraft.im.repository.InvoicePdfRepository;
import no.fjordkraft.im.services.UIStatementService;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by miles on 6/7/2017.
 */
@RestController
@RequestMapping("/api/statement")
public class IMStatementController {

    @Autowired
    UIStatementService statementService;

    @Autowired
    InvoicePdfRepository invoicePdfRepository;

    @RequestMapping(value = "details", method = RequestMethod.GET)
    @ResponseBody
    Map<String, Object> getDetails(@RequestParam(value = "states",required=false) String status,
                               @RequestParam(value = "fromTime", required=false) Timestamp fromTime,
                               @RequestParam(value = "toTime", required=false) Timestamp toTime,
                               @RequestParam(value = "customerID", required=false) String customerID,
                               @RequestParam(value = "brand", required=false) String brand,
                               @RequestParam(value = "invoiceNumber", required=false) String invoiceNumber,
                               @RequestParam(value = "accountNumber", required=false) String accountNumber,
                               @RequestParam(value = "page") int page,
                               @RequestParam(value = "size") int size) {
        List<RestStatement> restStatements = statementService.getDetails(page, size, status, fromTime, toTime, brand, customerID, invoiceNumber, accountNumber);
        Long count = getCountByStatus(status, fromTime, toTime, brand, customerID, invoiceNumber, accountNumber);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(IMConstants.STATEMENTS, restStatements);
        resultMap.put(IMConstants.TOTAL, count);
        return resultMap;
    }

    @RequestMapping(value = "count", method = RequestMethod.GET)
    @ResponseBody
    Long getCountByStatus(@RequestParam(value = "states",required=false) String status,
            @RequestParam(value = "fromTime", required=false) Timestamp fromTime,
            @RequestParam(value = "toTime", required=false) Timestamp toTime,
            @RequestParam(value = "brand", required=false) String brand,
            @RequestParam(value = "customerID", required=false) String customerID,
            @RequestParam(value = "invoiceNumber", required=false) String invoiceNumber,
            @RequestParam(value = "accountNumber", required=false) String accountNumber) {
        return statementService.getCountByStatus(status, fromTime, toTime, brand, customerID, invoiceNumber, accountNumber);
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
        return new ResponseEntity<byte[]>(invoicePdf.getPayload(), headers, HttpStatus.OK);
    }
}
