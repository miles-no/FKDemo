package no.fjordkraft.afi.server.emuxml.jpa.domain;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
public class EmuFilesRequest {

    private static final int MAX_DEFAULT_PAGE_SIZE = 10000;  // 10000 must be enough...

    private String filename;
    private Timestamp fromDate;
    private Timestamp toDate;
    private Pageable pageRequest;
    private Boolean allFiles;
    private Boolean invoiceReconciliation;

    public static EmuFilesRequest today(Pageable pageable) {
        EmuFilesRequest r = new EmuFilesRequest();
        DateTime today = new DateTime();
        r.setFromDate(new Timestamp(today.withTime(0, 0, 0, 0).getMillis()));
        r.setToDate(new Timestamp(today.withTime(23, 59, 59, 999).getMillis()));
        if (pageable == null) {
            pageable = new PageRequest(0, MAX_DEFAULT_PAGE_SIZE);
        }
        r.setPageRequest(pageable);
        return r;
    }

    public static EmuFilesRequest withPeriod(DateTime fromDate, DateTime toDate,
                                             Boolean allFiles,
                                             Boolean invoiceReconciliation,
                                             Pageable pageable) {
        EmuFilesRequest r = today(pageable);
        if (fromDate != null && toDate != null) {
            r.setFromDate(new Timestamp(fromDate.withTime(0, 0, 0, 0).getMillis()));
            r.setToDate(new Timestamp(toDate.withTime(23, 59, 59, 999).getMillis()));
        }
        r.setAllFiles(allFiles);
        r.setInvoiceReconciliation(invoiceReconciliation);
        return r;
    }

}