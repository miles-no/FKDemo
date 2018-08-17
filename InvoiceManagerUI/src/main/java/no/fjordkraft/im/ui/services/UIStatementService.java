package no.fjordkraft.im.ui.services;

import no.fjordkraft.im.domain.RestStatement;
import no.fjordkraft.im.model.StatusCount;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by miles on 8/10/2017.
 */
public interface UIStatementService {

    List<StatusCount> getStatementStatus();
    Long getTotalInvoiceCount();
    List<StatusCount> getStatusByCity(Timestamp fromTime, Timestamp toTime);
    List<StatusCount> getStatusByBrand(Timestamp fromTime, Timestamp toTime);
    Long getInvoiceCountByTime(Timestamp fromTime, Timestamp toTime);
    String getStatementById(Long id);
    List<RestStatement> getDetails(int page, int size, String status, Timestamp fromTime, Timestamp toTime,
                                   String brand, String customerID, String invoice, String accountNumber, String transferFileName, String legalPartClass, String creditLimit);
    Long getCountByStatus(String status, Timestamp fromTime,
                          Timestamp toTime, String brand, String customerID, String invoiceNumber,
                          String accountNumber, String transferFileName, String legalPartClass, String creditLimit);
    List<StatusCount> getStatusAndCountByTransferfile(String transferFile);
    List<StatusCount> getStatusByTransferfileBatchId(Long ekBatchJobId);
}
