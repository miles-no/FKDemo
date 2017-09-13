package no.fjordkraft.im.controller;

import no.fjordkraft.im.model.StatusCount;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.statusEnum.UIStatementStatusEnum;
import no.fjordkraft.im.services.UIStatementService;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by miles on 6/7/2017.
 */
@RestController
@RequestMapping("/api/dashboard")
public class IMDashboardController {

    @Autowired
    UIStatementService statementService;

    @RequestMapping(value = "status", method = RequestMethod.GET)
    @ResponseBody
    public List<StatusCount> getStatementCountByStatus() {
        List<StatusCount> statusCounts = statementService.getStatementStatus();
        List<StatusCount> uiStatusCount = new ArrayList<StatusCount>();
        StatusCount status = null;
        Long sum = 0l;

        Map<String, Long> statusMap = new HashMap<String, Long>();
        //Initialze statusMap
        statusMap.put(UIStatementStatusEnum.PENDING.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.PRE_PROCESSING.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.PROCESSING.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.MERGING.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.READY.getStatus(), 0l);
        statusMap.put(UIStatementStatusEnum.FAILED.getStatus(), 0l);

        for(StatusCount statusCount:statusCounts) {
            status = new StatusCount();
            if(statusCount.getName().equals(StatementStatusEnum.PENDING.getStatus())) {
                statusMap.put(UIStatementStatusEnum.PENDING.getStatus(), statusCount.getValue());
            } else if(statusCount.getName().equals(StatementStatusEnum.PRE_PROCESSING.getStatus())){
                statusMap.put(UIStatementStatusEnum.PRE_PROCESSING.getStatus(), statusCount.getValue());
            } else if(statusCount.getName().equals(StatementStatusEnum.PRE_PROCESSED.getStatus())
                    || statusCount.getName().equals(StatementStatusEnum.PDF_PROCESSING.getStatus())){
                sum = statusMap.get(UIStatementStatusEnum.PROCESSING.getStatus());
                sum += statusCount.getValue();
                statusMap.put(UIStatementStatusEnum.PROCESSING.getStatus(), sum);
            } else if(statusCount.getName().equals(StatementStatusEnum.PDF_PROCESSED.getStatus())
                    || statusCount.getName().equals(StatementStatusEnum.INVOICE_PROCESSING.getStatus())) {
                sum = statusMap.get(UIStatementStatusEnum.MERGING.getStatus());
                sum += statusCount.getValue();
                statusMap.put(UIStatementStatusEnum.MERGING.getStatus(), sum);
            } else if(statusCount.getName().equals(StatementStatusEnum.INVOICE_PROCESSED.getStatus())
                    || statusCount.getName().equals(StatementStatusEnum.DELIVERY_PENDING.getStatus())) {
                sum = statusMap.get(UIStatementStatusEnum.READY.getStatus());
                sum += statusCount.getValue();
                statusMap.put(UIStatementStatusEnum.READY.getStatus(), sum);
            } else if(statusCount.getName().equals(StatementStatusEnum.PRE_PROCESSING_FAILED.getStatus()) ||
                    statusCount.getName().equals(StatementStatusEnum.PDF_PROCESSING_FAILED.getStatus()) ||
                    statusCount.getName().equals(StatementStatusEnum.INVOICE_PROCESSING_FAILED.getStatus()) ||
                    statusCount.getName().equals(StatementStatusEnum.DELIVERY_FAILED.getStatus())) {

                sum = statusMap.get(UIStatementStatusEnum.FAILED.getStatus());
                sum += statusCount.getValue();
                statusMap.put(UIStatementStatusEnum.FAILED.getStatus(), sum);
            }
        }

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.PENDING.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.PENDING.getStatus()));
        uiStatusCount.add(0, status);

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.PRE_PROCESSING.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.PRE_PROCESSING.getStatus()));
        uiStatusCount.add(1, status);

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.PROCESSING.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.PROCESSING.getStatus()));
        uiStatusCount.add(2, status);

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.MERGING.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.MERGING.getStatus()));
        uiStatusCount.add(3, status);

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.READY.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.READY.getStatus()));
        uiStatusCount.add(4, status);

        status = new StatusCount();
        status.setName(UIStatementStatusEnum.FAILED.getStatus());
        status.setValue(statusMap.get(UIStatementStatusEnum.FAILED.getStatus()));
        uiStatusCount.add(5, status);

        status = new StatusCount();
        status.setName("Total");
        status.setValue(statementService.getTotalInvoiceCount());
        uiStatusCount.add(6, status);

        return uiStatusCount;
    }

    @RequestMapping(value = "city", method = RequestMethod.GET)
    @ResponseBody
    public List<StatusCount> getStatusByCity(@RequestParam("fromTime") Timestamp fromTime, @RequestParam("toTime") Timestamp toTime) {
        return statementService.getStatusByCity(fromTime, toTime);
    }

    @RequestMapping(value = "brand", method = RequestMethod.GET)
    @ResponseBody
    public List<StatusCount> getStatusByBrand(@RequestParam("fromTime") Timestamp fromTime, @RequestParam("toTime") Timestamp toTime) {
        return statementService.getStatusByBrand(fromTime, toTime);
    }

    @RequestMapping(value = "time",method = RequestMethod.GET)
    @ResponseBody
    public StatusCount getInvoiceCountByTime(@RequestParam("fromTime") Timestamp fromTime, @RequestParam("toTime") Timestamp toTime) {
        Long count = statementService.getInvoiceCountByTime(fromTime, toTime);
        StatusCount statusCount = new StatusCount();
        statusCount.setName("Total");
        statusCount.setValue(count);
        return statusCount;
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, List<StatusCount>> getOverviewData(@RequestParam("fromTime") Timestamp fromTime, @RequestParam("toTime") Timestamp toTime) {
        Map<String, List<StatusCount>> map = new HashMap<String, List<StatusCount>>();
        List<StatusCount> totalInvoice = new ArrayList<StatusCount>();
        totalInvoice.add(getInvoiceCountByTime(fromTime, toTime));
        List<StatusCount> statusByCity = getStatusByCity(fromTime, toTime);
        List<StatusCount> statusByBrand = getStatusByBrand(fromTime, toTime);

        map.put(IMConstants.TOTAL, totalInvoice);
        map.put(IMConstants.STATUS_BY_CITY, statusByCity);
        map.put(IMConstants.STATUS_BY_BRAND, statusByBrand);

        return map;
    }
}
