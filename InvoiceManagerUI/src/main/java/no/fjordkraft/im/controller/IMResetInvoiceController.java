package no.fjordkraft.im.controller;

import no.fjordkraft.im.services.SystemBatchInputService;
import no.fjordkraft.im.services.impl.InvoiceServiceImpl;
import no.fjordkraft.im.services.impl.StatementServiceImpl;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 5/27/19
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/api/reset")
public class IMResetInvoiceController {

    private static final Logger logger = LoggerFactory.getLogger(IMResetInvoiceController.class);

    @Autowired
    InvoiceServiceImpl invoiceService;

    @Autowired
    StatementServiceImpl statementService;

    @Autowired
    SystemBatchInputService systemBatchInputService;


    @RequestMapping(value = "invoice", method = RequestMethod.PUT)
    @ResponseBody
    void retryInvoiceProcessing() {
        logger.info("Called from Reset Invoice Controller");
        invoiceService.deleteInvoicePDFsByDate(new Date(System.currentTimeMillis()));
        statementService.updateAllStatementStatusToPending(StatementStatusEnum.PENDING);

    }
}
