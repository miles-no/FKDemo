package no.fjordkraft.im.services;


import no.fjordkraft.im.model.TransferFileArchive;

public interface UITransferFileArchiveService {

    /**
     * Fetch by key: transferType, brand, filename
     *
     * @param tfId
     * @return
     */

    TransferFileArchive save(TransferFileArchive transferFileArchive);

}
