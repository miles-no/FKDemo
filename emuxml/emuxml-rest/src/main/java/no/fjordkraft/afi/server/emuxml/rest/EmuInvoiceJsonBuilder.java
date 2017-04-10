package no.fjordkraft.afi.server.emuxml.rest;

import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.basis.rest.DateTimeJsonBuilder;
import no.fjordkraft.afi.server.basis.rest.PaginationJsonBuilder;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoice;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuInvoiceOrderLine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import java.util.List;

@Slf4j
public class EmuInvoiceJsonBuilder {

    private static DateTimeJsonBuilder dateTimeJsonBuilder = new DateTimeJsonBuilder();
    private PaginationJsonBuilder paginationJsonBuilder = new PaginationJsonBuilder();

    public JSONObject toJSON(Page<EmuInvoice> list) {
        JSONObject ret = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            if (list != null) {
                list.getContent().stream().forEach(tf -> jsonArray.put(toJSON(tf)));
                ret.put("emuInvoices", jsonArray);
                ret.put("pagination", paginationJsonBuilder.createJsonPagination(list));
            }
        } catch (JSONException e) {
            log.error("JSON Exception", e);
        }
        return ret;
    }

    private JSONObject toJSON(EmuInvoice obj) {
        JSONObject ret = new JSONObject();
        try {
            ret.put("invoiceNo", obj.getInvoiceNo());
            ret.put("ef_id", obj.getEf_id());
            ret.put("customerId", obj.getCustomerId());
            ret.put("brand", obj.getBrand());
            ret.put("accountNo", obj.getAccountNo());
            ret.put("invAgreementId", obj.getInvAgreementId());
            ret.put("printDate", dateTimeJsonBuilder.formatAsDate(obj.getPrintDate(), false));
            ret.put("giroAmount", obj.getGiroAmount());
            ret.put("printedAmount", obj.getPrintedAmount());
            ret.put("attachmentFileName", obj.getAttachmentFileName());
            if (obj.getInvoiceOrderLines() != null) {
                ret.put("invoiceOrderLines", toJSONInvoiceOrderLines(obj.getInvoiceOrderLines()));
            }
        } catch (JSONException e) {
            log.error("JSON Exception", e);
        }
        return ret;
    }

    private JSONArray toJSONInvoiceOrderLines(List<EmuInvoiceOrderLine> list) {
        JSONArray ret = new JSONArray();
        if (list != null) {
            try {
                list.stream().forEach(tf -> ret.put(toJSON(tf)));
            } catch (JSONException e) {
                log.error("JSON Exception", e);
            }
        }
        return ret;
    }

    private JSONObject toJSON(EmuInvoiceOrderLine obj) {
        JSONObject ret = new JSONObject();
        try {
            ret.put("invoiceNo", obj.getInvoiceNo());
            ret.put("eioid", obj.getEioid());
            ret.put("eiolid", obj.getEiolid());
            ret.put("startDate", dateTimeJsonBuilder.formatAsDate(obj.getStartDate(), false));
            ret.put("endDate", dateTimeJsonBuilder.formatAsDate(obj.getEndDate(), false));
            ret.put("kwh", obj.getKwh());
            ret.put("price", obj.getPrice());
            ret.put("amount", obj.getAmount());
        } catch (JSONException e) {
            log.error("JSON Exception", e);
        }
        return ret;
    }

}
