package no.fjordkraft.im.controller;

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
                                   @RequestParam(value = "actionOnId", required=false) Long actionOnId,
                                   @RequestParam(value = "logType", required=false) String logType) {

        List<AuditLog> auditLogList = auditLogService.getAuditLogRecords(page, size, fromTime, toTime, action, actionOnType, actionOnId, logType);
        Long count = auditLogService.getAuditLogRecordsCount(page, size, fromTime, toTime, action, actionOnType, actionOnId, logType);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(IMConstants.AUDIT_LOG, auditLogList);
        resultMap.put(IMConstants.TOTAL, count);
        return resultMap;
    }

}
