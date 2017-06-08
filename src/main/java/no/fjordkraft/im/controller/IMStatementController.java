package no.fjordkraft.im.controller;

import no.fjordkraft.im.domain.RestStatement;
import no.fjordkraft.im.services.impl.StatementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
