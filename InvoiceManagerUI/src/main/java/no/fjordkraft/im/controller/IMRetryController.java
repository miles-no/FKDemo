package no.fjordkraft.im.controller;

import no.fjordkraft.im.model.Statement;
import no.fjordkraft.im.services.impl.InvoiceServiceImpl;
import no.fjordkraft.im.services.impl.StatementServiceImpl;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by miles on 9/21/2017.
 */
@RestController
@RequestMapping("/api/retry")
public class IMRetryController {

    @Autowired
    InvoiceServiceImpl invoiceService;

    @Autowired
    StatementServiceImpl statementService;

    @RequestMapping(value = "statement", method = RequestMethod.PUT)
    @ResponseBody
    void retryInvoiceProcessing(@RequestParam("statementId") Long statementId) {

        invoiceService.deleteInvoicePDFsByStatementId(statementId);
        Statement statement = statementService.getStatement(statementId);
        statementService.updateStatement(statement, StatementStatusEnum.PENDING);
    }
}
