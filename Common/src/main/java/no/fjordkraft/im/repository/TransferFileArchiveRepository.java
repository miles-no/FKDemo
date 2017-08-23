package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.TransferFileArchive;
import no.fjordkraft.im.model.TransferFileId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bhavi on 6/27/2017.
 */
public interface TransferFileArchiveRepository extends JpaRepository<TransferFileArchive, TransferFileId> {
}