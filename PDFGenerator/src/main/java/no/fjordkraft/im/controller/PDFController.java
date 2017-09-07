package no.fjordkraft.im.controller;

import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.services.PDFGenerator;
import no.fjordkraft.im.services.StatementService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
