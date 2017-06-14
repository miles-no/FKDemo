package no.fjordkraft.im.repository;

/**
 * Created by miles on 5/4/2017.
 */

import no.fjordkraft.im.model.TransferFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferFileRepository extends JpaRepository<TransferFile,Long> {

    @Query("select t from TransferFile t where rownum <= 1 and t.transferState = :status order by t.id asc")
    TransferFile readSingleTransferFile(@Param("status") String status);

    @Query("select t from TransferFile t where t.transferState = :status order by t.id asc")
    List<TransferFile> readPendingTransferFiles(@Param("status") String status);
}
