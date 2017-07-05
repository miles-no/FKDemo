package no.fjordkraft.im.services;


import no.fjordkraft.im.model.TransferFileArchive;
import no.fjordkraft.im.model.TransferFileId;

public interface TransferFileArchiveService {

    /**
     * Fetch by key: transferType, brand, filename
     *
     * @param tfId
     * @return
     */
    TransferFileArchive findOne(TransferFileId tfId);

    TransferFileArchive save(TransferFileArchive transferFileArchive);

}
