package no.fjordkraft.im.controller;

import no.fjordkraft.im.domain.RestAuditLog;
import no.fjordkraft.im.domain.RestStatement;
import no.fjordkraft.im.model.AuditLog;
import no.fjordkraft.im.services.impl.AuditLogServiceImpl;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by miles on 7/24/2017.
 */
@RestController
@RequestMapping("/auditRecord")
public class IMAuditLogController {

    @Autowired
    AuditLogServiceImpl auditLogService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    Map<String, Object> getDetails(@RequestParam(value = "page") int page,
                                   @RequestParam(value = "size") int size,
                                   @RequestParam(value = "fromTime", required=false) Timestamp fromTime,
                                   @RequestParam(value = "toTime", required=false) Timestamp toTime,
                                   @RequestParam(value = "action", required=false) String action,
                                   @RequestParam(value = "actionOnType", required=false) String actionOnType,
                                   @RequestParam(value = "logType", required=false) String logType,
                                   @RequestParam(value = "invoiceNo", required=false) String invoiceNo,
                                   @RequestParam(value = "customerID", required=false) String customerID,
                                   @RequestParam(value = "accountNumber", required=false) String accountNumber) {

        List<RestAuditLog> auditLogList = auditLogService.getAuditLogRecords(page, size, fromTime, toTime, action, actionOnType, logType, invoiceNo, customerID, accountNumber);
        Long count = auditLogService.getAuditLogRecordsCount(fromTime, toTime, action, actionOnType, logType, invoiceNo, customerID, accountNumber);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(IMConstants.AUDIT_LOG, auditLogList);
        resultMap.put(IMConstants.TOTAL, count);
        return resultMap;
    }

}
