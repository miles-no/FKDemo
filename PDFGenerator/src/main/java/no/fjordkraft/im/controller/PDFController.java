package no.fjordkraft.im.controller;

import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.services.PDFGenerator;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by bhavi on 9/1/2017.
 */

@RestController
public class PDFController {

    @Autowired
    PDFGenerator pdfGenerator;


    @RequestMapping(value = "/pdf/{statementId}", method = RequestMethod.POST)
    public void generateInvoicePdf(@PathVariable("statementId") Long statementId){

        pdfGenerator.processStatement(statementId);

    }

    @RequestMapping(value = "/pdf", method = RequestMethod.POST)
    public void generateInvoicePdf(@RequestBody List<Long> statementIdList){

        for(Long statementId : statementIdList) {
            pdfGenerator.processStatement(statementId);
        }

    }
}
