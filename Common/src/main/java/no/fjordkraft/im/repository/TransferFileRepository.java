package no.fjordkraft.im.repository;

/**
 * Created by miles on 5/4/2017.
 */

import no.fjordkraft.im.model.TransferFile;
import no.fjordkraft.im.model.TransferFileId;
import no.fjordkraft.im.model.TransferTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferFileRepository extends JpaRepository<TransferFile,TransferFileId> {

    @Query("select t from TransferFile t where rownum <= 1 and t.imStatus = :status and t.transferType = :transferType order by t.fileStored ")
    TransferFile readSingleTransferFile(@Param("status") String status, @Param("transferType") TransferTypeEnum transferType);

    @Query("select t from TransferFile t where rownum <= 1 and t.imStatus is null and t.fileStored is not null " +
            "and t.transferType = :transferType order by t.fileStored asc")
    TransferFile readSingleTransferFile(@Param("transferType") TransferTypeEnum transferType);

    @Query("select t from TransferFile t where rownum <= 1 and t.imStatus = :status and t.transferType = :transferType " +
            "order by t.fileStored asc")
    List<TransferFile> readPendingTransferFiles(@Param("status") String status, @Param("transferType") TransferTypeEnum transferType);

    @Query("select t from TransferFile t where t.ekBatchJobId = :ekBatchJobId and t.transferType = :transferType")
    List<TransferFile> readTransferfileByBatchJobId(@Param("ekBatchJobId") Long ekBatchJobId, @Param("transferType") TransferTypeEnum transferType);

    @Query("select t from TransferFile t where rownum <= :rownum and t.imStatus is null and t.fileStored is not null and t.invoiceManager = 1 " +
            "and t.transferType = :transferType order by t.fileStored asc")
    List<TransferFile> readTransferFileForAutoMode(@Param("rownum") Long rownum, @Param("transferType") TransferTypeEnum transferType);

}
