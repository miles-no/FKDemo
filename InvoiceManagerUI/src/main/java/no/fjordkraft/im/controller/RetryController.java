package no.fjordkraft.im.controller;

import org.springframework.web.bind.annotation.*;

/**
 * Created by miles on 7/25/2017.
 */
@RestController
@RequestMapping("/api/retry")
public class RetryController {

    @RequestMapping(value = "invoiceFeedWatcher", method = RequestMethod.PUT)
    @ResponseBody
    void retryInvoiceFeedWatch(@RequestParam("transfertype") String transfertype,
                                  @RequestParam("brand") String brand,
                                  @RequestParam("filename") String filename) {

    }

    @RequestMapping(value = "invoiceFeedExtracter", method = RequestMethod.PUT)
    @ResponseBody
    void retryInvoiceFeedExtract(@RequestParam("transfertype") String transfertype,
                                  @RequestParam("brand") String brand,
                                  @RequestParam("filename") String filename) {

    }

    @RequestMapping(value = "invoiceProcessing", method = RequestMethod.PUT)
    @ResponseBody
    void retryInvoiceProcessing(@RequestParam("statementId") Long statementId) {

    }
}
