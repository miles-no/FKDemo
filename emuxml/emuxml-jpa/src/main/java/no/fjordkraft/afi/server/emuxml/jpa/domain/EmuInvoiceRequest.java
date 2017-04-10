package no.fjordkraft.afi.server.emuxml.jpa.domain;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;

@Data
public class EmuInvoiceRequest {

    private static final int MAX_DEFAULT_PAGE_SIZE = 10000;  // 10000 must be enough...

    private String accountNo;
    private Timestamp fromDate;
    private Timestamp toDate;
    private Pageable pageRequest;
    private boolean fetchInvoiceOrderLines;
    private boolean checkExported;  // TRUE to check if the invoice has been produced and ex

    public static EmuInvoiceRequest today(String accountNo, Pageable pageable) {
        EmuInvoiceRequest r = new EmuInvoiceRequest();
        r.setAccountNo(accountNo);
        DateTime today = new DateTime();
        r.setFromDate(new Timestamp(today.withTime(0, 0, 0, 0).getMillis()));
        r.setToDate(new Timestamp(today.withTime(23, 59, 59, 999).getMillis()));
        if (pageable == null) {
            pageable = new PageRequest(0, MAX_DEFAULT_PAGE_SIZE);
        }
        r.setPageRequest(pageable);
        r.setFetchInvoiceOrderLines(true);
        return r;
    }

    public static EmuInvoiceRequest withPeriod(String accountNo,
                                               DateTime fromDate, DateTime toDate,
                                               Pageable pageable) {
        EmuInvoiceRequest r = today(accountNo, pageable);
        if (fromDate != null && toDate != null) {
            r.setFromDate(new Timestamp(fromDate.withTime(0, 0, 0, 0).getMillis()));
            r.setToDate(new Timestamp(toDate.withTime(23, 59, 59, 999).getMillis()));
        }
        return r;
    }

    public static EmuInvoiceRequest allForAccount(String accountNo) {
        EmuInvoiceRequest r = new EmuInvoiceRequest();
        r.setAccountNo(accountNo);
        r.setPageRequest(new PageRequest(0, MAX_DEFAULT_PAGE_SIZE));
        r.setFetchInvoiceOrderLines(true);
        return r;
    }
}