package no.fjordkraft.im.ui.services;


import no.fjordkraft.im.model.TransferFileArchive;
import no.fjordkraft.im.model.TransferFileId;

public interface UITransferFileArchiveService {

    /**
     * Fetch by key: transferType, brand, filename
     *
     * @param tfId
     * @return
     */

    TransferFileArchive save(TransferFileArchive transferFileArchive);

}
