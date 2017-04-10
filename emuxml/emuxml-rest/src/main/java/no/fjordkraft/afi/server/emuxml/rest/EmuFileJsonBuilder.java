package no.fjordkraft.afi.server.emuxml.rest;

import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.basis.rest.DateTimeJsonBuilder;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFile;
import no.fjordkraft.afi.server.iscuclient.jpa.domain.InvoiceReconciliation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@Slf4j
public class EmuFileJsonBuilder {

    private static DateTimeJsonBuilder dateTimeJsonBuilder = new DateTimeJsonBuilder();

    public JSONObject toJSON(List<EmuFile> list) {
        JSONObject ret = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            list.stream().forEach(tf -> jsonArray.put(toJSON(tf)));
            ret.put("emuFiles", jsonArray);
        } catch (JSONException e) {
            log.error("JSON Exception", e);
        }
        return ret;
    }

    public JSONObject toJSON(EmuFile obj) {
        JSONObject ret = new JSONObject();
        try {
            ret.put("id", obj.getId());
            ret.put("filename", obj.getFilename());
            ret.put("reportId", obj.getReportId());
            ret.put("orderNo", obj.getOrderNo());
            ret.put("productionDate", dateTimeJsonBuilder.formatAsDate(obj.getProductionDate(), false));
            ret.put("numberOfInvoices", obj.getNumberOfInvoices());
            ret.put("sumOfGiroAmount", obj.getSumOfGiroAmount());
            ret.put("sumOfPrintedAmount", obj.getSumOfPrintedAmount());
            if (obj.getStatus() != null) {
                ret.put("status", obj.getStatus().name());
            }
            if (obj.getScanned() != null) {
                ret.put("scanned", dateTimeJsonBuilder.formatAsDateTime(obj.getScanned(), false));
            }
            ret.put("fileSize", obj.getFileSize());
            if (obj.getInvoiceReconciliation() != null) {
                ret.put("ir", toJSON(obj.getInvoiceReconciliation()));
                Boolean valid = isValid(obj, obj.getInvoiceReconciliation());
                if (valid != null) {
                    ret.put("valid", valid);
                }
            }
        } catch (JSONException e) {
            log.error("JSON Exception", e);
        }
        return ret;
    }

    private Boolean isValid(EmuFile ef, InvoiceReconciliation ir) {
        if (ef == null ||
                ir == null ||
                ef.getNumberOfInvoices() == null ||
                ir.getNumberOfInvoices() == null ||
                ef.getSumOfGiroAmount() == null ||
                ir.getSumOfGiroAmount() == null) {
            return null;
        }
        // Checks printed amount against invoice reconciliation giro amount..
        return ef.getNumberOfInvoices().equals(ir.getNumberOfInvoices()) &&
                ef.getSumOfPrintedAmount().equals(ir.getSumOfGiroAmount());
    }

    public JSONObject toJSON(InvoiceReconciliation obj) {
        JSONObject ret = new JSONObject();
        try {
            ret.put("id", obj.getId());
            ret.put("filename", obj.getFilename());
            ret.put("reportId", obj.getReportId());
            ret.put("orderNo", obj.getOrderNo());
            ret.put("productionDate", dateTimeJsonBuilder.formatAsDate(obj.getProductionDate(), false));
            ret.put("numberOfInvoices", obj.getNumberOfInvoices());
            ret.put("sumOfGiroAmount", obj.getSumOfGiroAmount());
            ret.put("sumOfPrintedAmount", obj.getSumOfPrintedAmount());
            ret.put("listType", obj.getListType());
            ret.put("added", dateTimeJsonBuilder.formatAsDateTime(obj.getAdded(), false));
        } catch (JSONException e) {
            log.error("JSON Exception", e);
        }
        return ret;
    }
}
